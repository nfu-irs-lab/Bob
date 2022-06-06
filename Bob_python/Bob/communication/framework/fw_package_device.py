import abc
from abc import ABC
from threading import Thread
from typing import Optional

from Bob.device.framework.fw_device import Device


class PackageListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def onReceive(self, data: str):
        pass


class PackageDevice(Device, ABC, Thread):

    def __init__(self):
        super().__init__()
        self.__listener: Optional[PackageListener] = None

    @abc.abstractmethod
    def writeString(self, package: str):
        pass

    @abc.abstractmethod
    def write(self, package: bytes):
        pass

    def setListener(self, listener: PackageListener):
        self.__listener = listener

    def _onReceive(self, data: str):
        if self.__listener is not None:
            self.__listener.onReceive(data)
