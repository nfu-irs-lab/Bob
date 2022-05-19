import abc
from abc import ABC


class Detector(ABC):

    @abc.abstractmethod
    def detect(self, image):
        pass
