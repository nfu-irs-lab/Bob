"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""
import sys
from pathlib import Path
from typing import List, Optional

import cv2
import torch
import torch.backends.cudnn as cudnn
from detector.framework.detector import Detector, DetectListener

FILE = Path(__file__).absolute()
sys.path.append(FILE.parents[0].as_posix())  # add yolov5/ to path

from models.experimental import attempt_load
from utils.datasets import LoadStreams, LoadImages
from utils.general import check_img_size, check_requirements, check_imshow, colorstr, non_max_suppression, \
    apply_classifier, scale_coords, xyxy2xywh, strip_optimizer, set_logging, increment_path, save_one_box
from utils.plots import colors, plot_one_box
from utils.torch_utils import select_device, load_classifier, time_sync

FILE = Path(__file__).absolute()
sys.path.append(FILE.parents[0].as_posix())  # add yolov5/ to path


def loadDataset(webcam: bool, source, imgsz, stride):
    if webcam:
        cudnn.benchmark = True  # set True to speed up constant image size inference
        return LoadStreams(source, img_size=imgsz, stride=stride)
    else:
        return LoadImages(source, img_size=imgsz, stride=stride)
    pass


class ObjectDetector(Detector):

    def __init__(self, listener: DetectListener):
        super().__init__(listener)

    @torch.no_grad()
    def _detect(self,
                weights='yolov5s.pt',  # model.pt path(s)
                source='0',  # file/dir/URL/glob, 0 for webcam
                imgsz=640,  # inference size (pixels)
                conf_thres=0.25,  # confidence threshold
                iou_thres=0.45,  # NMS IOU threshold
                max_det=1000,  # maximum detections per image
                device='',  # cuda device, i.e. 0 or 0,1,2,3 or cpu
                view_img=False,  # show results
                save_txt=False,  # save results to *.txt
                classes=None,  # filter by class: --class 0, or --class 0 2 3
                agnostic_nms=False,  # class-agnostic NMS
                augment=False,  # augmented inference
                visualize=False,  # visualize features
                update=False,  # update all models
                project='runs/detect',  # save results to project/name
                name='exp',  # save results to project/name
                exist_ok=False,  # existing project/name ok, do not increment
                line_thickness=3,  # bounding box thickness (pixels)
                hide_labels=False,  # hide labels
                hide_conf=False,  # hide confidences
                half=False,  # use FP16 half-precision inference
                ):

        # save_img = not nosave and not source.endswith('.txt')  # save inference images
        webcam = source.isnumeric() or source.endswith('.txt') or source.lower().startswith(
            ('rtsp://', 'rtmp://', 'http://', 'https://'))
        # Directories
        save_dir = increment_path(Path(project) / name, exist_ok=exist_ok)  # increment run
        (save_dir / 'labels' if save_txt else save_dir).mkdir(parents=True, exist_ok=True)  # make dir

        # Initialize
        set_logging()
        device = select_device(device)
        half &= device.type != 'cpu'  # half precision only supported on CUDA

        # Load model
        w = weights[0] if isinstance(weights, list) else weights
        classify, pt, onnx = False, w.endswith('.pt'), w.endswith('.onnx')  # inference type
        stride, names = 64, [f'class{i}' for i in range(1000)]  # assign defaults
        if pt:
            model = attempt_load(weights, map_location=device)  # load FP32 model
            stride = int(model.stride.max())  # model stride
            names = model.module.names if hasattr(model, 'module') else model.names  # get class names
            if half:
                model.half()  # to FP16
            if classify:  # second-stage classifier
                modelc = load_classifier(name='resnet50', n=2)  # initialize
                modelc.load_state_dict(torch.load('resnet50.pt', map_location=device)['model']).to(device).eval()
        elif onnx:
            check_requirements(('onnx', 'onnxruntime'))
            import onnxruntime
            session = onnxruntime.InferenceSession(w, None)
        imgsz = check_img_size(imgsz, s=stride)  # check image size

        # Dataloader
        if webcam:
            view_img = check_imshow()

        dataset = loadDataset(webcam, source, imgsz, stride)

        # Run inference
        if pt and device.type != 'cpu':
            model(torch.zeros(1, 3, imgsz, imgsz).to(device).type_as(next(model.parameters())))  # run once
        for path, img, im0s, vid_cap in dataset:
            if self._interrupted():
                break
            if not self._running():
                continue

            if pt:
                img = torch.from_numpy(img).to(device)
                img = img.half() if half else img.float()  # uint8 to fp16/32
            elif onnx:
                img = img.astype('float32')
            img /= 255.0  # 0 - 255 to 0.0 - 1.0
            if len(img.shape) == 3:
                img = img[None]  # expand for batch dim

            # Inference
            if pt:
                visualize = increment_path(save_dir / Path(path).stem, mkdir=True) if visualize else False
                pred = model(img, augment=augment, visualize=visualize)[0]
            elif onnx:
                pred = torch.tensor(
                    session._running([session.get_outputs()[0].name], {session.get_inputs()[0].name: img}))

            # NMS
            pred = non_max_suppression(pred, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)

            # Second-stage classifier (optional)
            if classify:
                pred = apply_classifier(pred, modelc, img, im0s)

            # Process predictions
            for i, det in enumerate(pred):  # detections per image
                if webcam:  # batch_size >= 1
                    p, im0, frame = path[i], im0s[i].copy(), dataset.count
                else:
                    p, im0, frame = path, im0s.copy(), getattr(dataset, 'frame', 0)

                p = Path(p)  # to Path

                detectedObjectList: List = []
                if len(det):
                    # Rescale boxes from img_size to im0 size
                    det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()
                    # Print results
                    for c in det[:, -1].unique():
                        n = (det[:, -1] == c).sum()  # detections per class

                    print("Detected:", end='\t')
                    # Write results
                    for *xyxy, conf, cls in reversed(det):
                        c = int(cls)  # integer class
                        print('%s(%0.2f)' % (names[c], conf), end=',')
                        detectedObjectList.append({'name': names[c], 'confidence': float(conf)})
                        if view_img:  # add bbox to image
                            label = None if hide_labels else (names[c] if hide_conf else f'{names[c]} {conf:.2f}')
                            plot_one_box(xyxy, im0, label=label, color=colors(c, True), line_thickness=line_thickness)
                    print(end='\n')

                # Stream results
                if view_img:
                    cv2.imshow(str(p), im0)
                    cv2.waitKey(1)  # 1 millisecond

                self._listener.onDetect(detectedObjectList)

        if update:
            strip_optimizer(weights)  # update model (to fix SourceChangeWarning)

        cv2.destroyAllWindows()

