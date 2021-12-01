import abc
from abc import ABC


class Device(ABC):
    @abc.abstractmethod
    def open(self):
        pass

    @abc.abstractmethod
    def close(self):
        pass

    @abc.abstractmethod
    def isOpen(self) -> bool:
        pass


class SerialDevice(Device, ABC):

    @abc.abstractmethod
    def read(self, n: int) -> bytes:
        pass

    @abc.abstractmethod
    def write(self, data: bytes) -> int:
        pass
