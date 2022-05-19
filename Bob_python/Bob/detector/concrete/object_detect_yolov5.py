from typing import List

from Bob.detector.framework.detector import Detector
import torch


class ObjectDetector(Detector):

    def __init__(self, iou: float = 0.5, conf: float = 0.5):
        model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)
        model.iou = iou
        model.conf = conf
        self._module = model

    def detect(self, image):
        detections = self._module(image)

        r = detections.pandas().xyxy[0]
        results: List = []
        for i in range(0, len(r.name.values)):
            name = r.name.values[i]

            xmax = int(r.xmax.values[i])
            ymax = int(r.ymax.values[i])

            xmin = int(r.xmin.values[i])
            ymin = int(r.ymin.values[i])

            results.append({'name': name, 'x': {'min': xmin, 'max': xmax}, 'y': {'min': ymin, 'max': ymax}})

        return results
