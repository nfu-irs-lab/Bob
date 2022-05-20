from typing import Optional, List

import cv2
from deepface import DeepFace
from Bob.visual.detector.framework.detector import Detector


class FaceDetector(Detector):

    def __init__(self, _id):
        super().__init__(_id)
        self._faceCascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

    def detect(self, frame) -> List:
        results: List = []
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = self._faceCascade.detectMultiScale(gray, 1.1, 4)

        for (x, y, w, h) in faces:
            xmax = x+w
            ymax = y+h

            xmin = x
            ymin = y
            face = frame[ymin:ymax, xmin:xmax]
            try:
                emotion = DeepFace.analyze(face, actions=['emotion'])['dominant_emotion']
                results.append({'emotion': emotion, 'x': {'min': xmin, 'max': xmax}, 'y': {'min': ymin, 'max': ymax}})
            except Exception as e:
                pass
        return results
