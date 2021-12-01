import abc
import threading

from Bob.device.framework.fw_device import Device


class SerialReadStrategy(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def warp(self, data: bytes):
        pass

    @abc.abstractmethod
    def isIntegralPackage(self) -> bool:
        pass

    @abc.abstractmethod
    def getPackage(self) -> bytes:
        pass


class SerialListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def onReceive(self, data: bytes):
        pass


class PackageMonitor(threading.Thread, abc.ABC):
    def __init__(self, listener: SerialListener, strategy: SerialReadStrategy):
        super().__init__()
        self._listener = listener
        self._strategy = strategy

    @abc.abstractmethod
    def stop(self):
        pass
