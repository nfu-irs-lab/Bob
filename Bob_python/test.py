import cv2

from Bob.detector.concrete.face_detect_deepface import FaceDetector
from Bob.detector.concrete.object_detect_yolov5 import ObjectDetector

cam = cv2.VideoCapture(0)

object_detector = ObjectDetector(0.3)
# face_detector = FaceDetector()

while True:
    ret, frame = cam.read()

    for result in object_detector.detect(frame):
        cv2.rectangle(frame, (result['x']['min'], result['y']['min']), (result['x']['max'], result['y']['max']),
                      (0, 255, 0), 2)
        font = cv2.FONT_HERSHEY_SIMPLEX
        cv2.putText(frame, result['name'], (result['x']['min'], result['y']['min']-10), font, 1, (0, 0, 255), 2, cv2.LINE_4)
        print(result)

    cv2.imshow('result', frame)
    print("------------------")
    cv2.waitKey(1)
