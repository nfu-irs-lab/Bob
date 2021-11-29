import abc
from abc import ABC


class DetectListener(ABC):
    @abc.abstractmethod
    def onDetect(self, data):
        pass


class Detector(ABC):
    def __init__(self, listener: DetectListener):
        self.listener = listener

    @abc.abstractmethod
    def detect(self):
        pass

    @abc.abstractmethod
    def start(self):
        pass

    @abc.abstractmethod
    def pause(self):
        pass

    @abc.abstractmethod
    def stop(self):
        pass
