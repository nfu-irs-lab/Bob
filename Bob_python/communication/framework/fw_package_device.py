import abc
from abc import ABC

from Bob.device.framework.fw_device import Device
from communication.framework.fw_listener import PackageListener
from communication.framework.fw_strategy import SerialReadStrategy
from communication.framework.fw_package import Package


class PackageDevice(Device, ABC):
    @abc.abstractmethod
    def writePackage(self, package: Package):
        pass

    @abc.abstractmethod
    def getMonitor(self, listener: PackageListener, strategy: SerialReadStrategy):
        pass
