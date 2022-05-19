from typing import Optional, List

import cv2
from deepface import DeepFace
from Bob.visual.detector.framework.detector import Detector


class FaceDetector(Detector):

    def __init__(self):
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
                print(e.__str__())
                pass

        # cv2.imshow('result', frame)
        return results

# cv2.waitKey(1)
# face_type = result['dominant_emotion']

# result = DeepFace.analyze(frame, actions=['emotion'])
# return result['dominant_emotion']

# def _detect(self):
#     cap = cv2.VideoCapture(0)
#     cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
#     while True:
#         if self._interrupted():
#             break
#         if not self._running():
#             continue
#         ret, frame = cap.read()
#         cv2.imshow('raw', frame)
#         try:
#             result = DeepFace.analyze(frame, actions=['emotion'])
#             gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
#             faces = self._faceCascade.detectMultiScale(gray, 1.1, 4)
#             for (x, y, w, h) in faces:
#                 cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
#             font = cv2.FONT_HERSHEY_SIMPLEX
#             cv2.putText(frame, result['dominant_emotion'], (50, 50), font, 3, (0, 0, 255), 2, cv2.LINE_4)
#             cv2.imshow('result', frame)
#             face_type = result['dominant_emotion']
#             self._listener.onDetect(face_type)
#         except Exception as e:
#             print(e.__str__())
#
#         if cv2.waitKey(2) & 0xFF == ord('q'):
#             break
#     print("Release webcam")
#     cap.release()
#     print("Destroy windows")
#     cv2.destroyAllWindows()
