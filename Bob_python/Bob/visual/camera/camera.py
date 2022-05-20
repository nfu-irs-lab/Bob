import abc
import threading
from abc import ABC
from typing import List

import cv2

from Bob.visual.detector.framework.detector import Detector


class CameraListener(ABC):
    @abc.abstractmethod
    def onImageRead(self, image):
        pass

    @abc.abstractmethod
    def onDetect(self, _id, image, data):
        pass


class CameraMonitor(threading.Thread):

    def __init__(self):
        super().__init__()
        self._detectors: List[Detector] = []
        self._listener: CameraListener = None
        self._webcam = cv2.VideoCapture(0)
        self._opened = True
        self._detector_enablers: List[bool] = []

    def registerDetector(self, detector: Detector, enable: bool):
        self.setDetectorEnable(detector.getId(), enable)
        self._detectors.append(detector)

    def setDetectorEnable(self, detectorId, enable: bool):
        if enable:
            self._detector_enablers.append(detectorId)
        else:
            try:
                self._detector_enablers.remove(detectorId)
            except:
                pass

    def setListener(self, listener: CameraListener):
        self._listener = listener

    def run(self):
        while self._opened:
            ret, frame = self._webcam.read()
            if not ret:
                continue
            if not self._listener is None:
                self._listener.onImageRead(frame)

            if len(self._detectors) != 0:
                self._detect(frame)

            cv2.waitKey(1)
        self._webcam.release()

    def _detect(self, image):
        for detector in self._detectors:
            if not self._isEnable(detector.getId()):
                continue

            result = detector.detect(image)
            if len(result) != 0:
                if not self._listener is None:
                    self._listener.onDetect(detector.getId(), image, result)

    def _isEnable(self, detectorId):
        for enable_id in self._detector_enablers:
            if enable_id == detectorId:
                return True
        return False

    def stop(self):
        self._opened = False
