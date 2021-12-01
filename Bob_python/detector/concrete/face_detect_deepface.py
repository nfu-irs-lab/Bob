import time
import cv2
from deepface import DeepFace
from detector.framework.detector import Detector, DetectListener


class FaceDetector(Detector):
    def __init__(self,listener:DetectListener):
        super().__init__(listener)
        self.__start = False
        self.__interrupt = False

    def start(self):
        self.__start = True

    def pause(self):
        self.__start = False

    def stop(self):
        self.__interrupt = True
        pass

    def detect(self):
        faceCascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
        cap = cv2.VideoCapture(0)
        cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
        while not self.__interrupt:
            while not self.__start:
                time.sleep(1)
            ret, frame = cap.read()
            cv2.imshow('raw', frame)
            try:
                result = DeepFace.analyze(frame, actions=['emotion'])
                gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

                faces = faceCascade.detectMultiScale(gray, 1.1, 4)

                for (x, y, w, h) in faces:
                    cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

                font = cv2.FONT_HERSHEY_SIMPLEX

                cv2.putText(frame, result['dominant_emotion'], (50, 50), font, 3, (0, 0, 255), 2, cv2.LINE_4)

                cv2.imshow('result', frame)
                face_type = result['dominant_emotion']
                self._listener.onDetect(face_type)
            except Exception as e:
                print(e.__str__())
            if cv2.waitKey(2) & 0xFF == ord('q'):
                break
        cap.release()
        cv2.destroyWindow()
