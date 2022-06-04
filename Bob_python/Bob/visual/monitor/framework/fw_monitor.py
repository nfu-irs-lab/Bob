import abc
import threading
from abc import ABC
from typing import List

from Bob.visual.detector.framework.detector import Detector


class CameraListener(ABC):
    @abc.abstractmethod
    def onImageRead(self, image):
        pass

    @abc.abstractmethod
    def onDetect(self, _id, image, data):
        pass


class VideoMonitor(threading.Thread):

    def __init__(self):
        super().__init__()
        self.__detectors: List[Detector] = []
        self._listener: CameraListener = None
        self.__opened = True
        self.__detector_enablers: List[bool] = []

    def registerDetector(self, detector: Detector, enable: bool):
        self.setDetectorEnable(detector.getId(), enable)
        self.__detectors.append(detector)

    def setDetectorEnable(self, detectorId, enable: bool):
        if enable:
            self.__detector_enablers.append(detectorId)
        else:
            try:
                self.__detector_enablers.remove(detectorId)
            except:
                pass

    def setListener(self, listener: CameraListener):
        self._listener = listener

    def _detect(self, image):
        for detector in self.__detectors:
            if not self._isEnable(detector.getId()):
                continue

            result = detector.detect(image)
            if len(result) != 0:
                if not self._listener is None:
                    self._listener.onDetect(detector.getId(), image, result)

    def _isEnable(self, detectorId):
        for enable_id in self.__detector_enablers:
            if enable_id == detectorId:
                return True
        return False

    def stop(self):
        self.__opened = False

    def isOpen(self):
        return self.__opened
