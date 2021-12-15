from Bob.communication.framework.fw_listener import PackageListener
from Bob.communication.framework.fw_monitor import PackageMonitor
from Bob.communication.framework.fw_package_device import PackageDevice
from Bob.communication.framework.fw_strategy import SerialReadStrategy
from Bob.device.framework.fw_device import SerialDevice
from Bob.communication.concrete.crt_monitor import SerialPackageMonitor
from Bob.communication.framework.fw_package import Package


class SerialPackageDevice(PackageDevice):

    def __init__(self, ser: SerialDevice):
        self.ser = ser

    def open(self):
        if not self.ser.isOpen():
            self.ser.open()

    def close(self):
        self.ser.close()
        pass

    def isOpen(self):
        return self.ser.isOpen()

    def writePackage(self, package: Package) -> int:
        return self.ser.write(package.getBytes())

    def getMonitor(self, listener: PackageListener,
                   strategy: SerialReadStrategy) -> PackageMonitor:
        return SerialPackageMonitor(self.ser, listener, strategy)
