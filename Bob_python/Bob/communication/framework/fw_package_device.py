import abc
from abc import ABC

from Bob.communication.framework.fw_listener import PackageListener
from Bob.communication.framework.fw_package import Package
from Bob.communication.framework.fw_strategy import SerialReadStrategy
from Bob.device.framework.fw_device import Device


class PackageDevice(Device, ABC):
    @abc.abstractmethod
    def writePackage(self, package: Package):
        pass

    @abc.abstractmethod
    def getMonitor(self, listener: PackageListener, strategy: SerialReadStrategy):
        pass
