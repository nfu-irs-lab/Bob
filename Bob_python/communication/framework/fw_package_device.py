import abc
from abc import ABC

from Bob.device.framework.fw_device import Device
from communication.framework.fw_monitor import SerialListener, SerialReadStrategy, PackageMonitor
from communication.framework.fw_package import Package


class PackageDevice(Device, ABC):

    @abc.abstractmethod
    def writePackage(self, package: Package):
        pass

    @abc.abstractmethod
    def getMonitor(self, listener: SerialListener, strategy: SerialReadStrategy) -> PackageMonitor:
        pass
