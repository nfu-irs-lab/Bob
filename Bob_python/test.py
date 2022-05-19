import cv2
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.utils import visual_utils

cam = cv2.VideoCapture(0)

object_detector = ObjectDetector(0.3)
face_detector = FaceDetector()
while True:
    ret, frame = cam.read()

    for result in object_detector.detect(frame):
        label = result['name'] + " " + str(round(result['conf'], 2))
        visual_utils.annotateLabel(frame, (result['x']['min'], result['y']['min']),
                                   (result['x']['max'], result['y']['max']), label)
    cv2.imshow('result', frame)
    cv2.waitKey(1)
