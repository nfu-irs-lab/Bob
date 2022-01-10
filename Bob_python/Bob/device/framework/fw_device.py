import abc
import time
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
    def __init__(self, write_delay_ms: int):
        self.__write_delay_ms = write_delay_ms

    @abc.abstractmethod
    def read(self, n: int) -> bytes:
        pass

    def write(self, data: bytes) -> int:
        i = self._write_without_delay(data)
        if self.__write_delay_ms != 0:
            time.sleep(self.__write_delay_ms / 1000.)

        return i

    @abc.abstractmethod
    def _write_without_delay(self, data: bytes) -> int:
        pass
