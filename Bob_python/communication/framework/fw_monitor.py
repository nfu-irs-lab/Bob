import abc
import threading

from communication.framework.fw_package_device import PackageDevice


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


class SerialListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def onReceive(self, device: PackageDevice, data: bytes):
        pass


class PackageMonitor(threading.Thread, abc.ABC):
    def __init__(self, device: PackageDevice, listener: SerialListener, strategy: SerialReadStrategy):
        super().__init__()
        self._package_device = device
        self._listener = listener
        self._strategy = strategy

    @abc.abstractmethod
    def stop(self):
        pass
