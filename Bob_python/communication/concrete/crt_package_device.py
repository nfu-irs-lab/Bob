from Bob.device.framework.fw_device import SerialDevice
from communication.concrete.crt_monitor import SerialPackageMonitor
from communication.framework.fw_monitor import SerialListener, SerialReadStrategy
from communication.framework.fw_package import Package
from communication.framework.fw_package_device import PackageDevice


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

    def getMonitor(self, listener: SerialListener, strategy: SerialReadStrategy):
        return SerialPackageMonitor(self.ser, listener, strategy)
