import abc
from abc import ABC


class Detector(ABC):
    def __init__(self, _id):
        self._id = _id

    @abc.abstractmethod
    def detect(self, image):
        pass

    def getId(self):
        return self._id
