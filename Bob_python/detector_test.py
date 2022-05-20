import cv2

from Bob.visual.camera.camera import CameraMonitor, CameraListener
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.utils import visual_utils


class TestListener(CameraListener):

    def onImageRead(self, image):
        cv2.imshow("face", image)

    def onDetect(self, detector_id, image, data):
        if detector_id == 1:
            labeledImage = image
            for result in data:
                label = result['emotion']
                labeledImage = visual_utils.annotateLabel(labeledImage, (result['x']['min'], result['y']['min']),
                                                          (result['x']['max'], result['y']['max']), label,
                                                          overwrite=False)
            cv2.imshow("face", labeledImage)
        elif detector_id == 2:
            labeledImage = image
            for result in data:
                label = result['name'] + " " + str(round(result['conf'], 2))
                labeledImage = visual_utils.annotateLabel(image, (result['x']['min'], result['y']['min']),
                                                          (result['x']['max'], result['y']['max']), label,
                                                          overwrite=False)
            cv2.imshow("object", labeledImage)


monitor = CameraMonitor()
monitor.setDetector([FaceDetector(1), ObjectDetector(2)])
monitor.setListener(TestListener())
monitor.start()

# while True:
#     ret, frame = cam.read()
#     face_detector.detect(frame)
#
#     for result in face_detector.detect(frame):
#         label = result['emotion']
#         visual_utils.annotateLabel(frame, (result['x']['min'], result['y']['min']),
#                                    (result['x']['max'], result['y']['max']), label)
#     cv2.imshow("result", frame)
#     cv2.waitKey(1)
