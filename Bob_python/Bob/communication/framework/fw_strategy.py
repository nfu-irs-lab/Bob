import abc


class SerialReadStrategy(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def warp(self, data: bytes):
        pass

    @abc.abstractmethod
    def hasNextPackage(self) -> bool:
        pass

    @abc.abstractmethod
    def nextPackage(self) -> bytes:
        pass
