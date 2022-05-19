import cv2
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.utils import visual_utils

cam = cv2.VideoCapture(0)

face_detector = FaceDetector()
while True:
    ret, frame = cam.read()
    face_detector.detect(frame)

    for result in face_detector.detect(frame):
        label = result['emotion']
        visual_utils.annotateLabel(frame, (result['x']['min'], result['y']['min']),
                                   (result['x']['max'], result['y']['max']), label)
    cv2.imshow("result", frame)
    cv2.waitKey(1)
