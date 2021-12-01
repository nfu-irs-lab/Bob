
from serial import Serial

from Bob.device.framework.fw_device import SerialDevice


class LocalSerialDevice(SerialDevice):

    def __init__(self, serial: Serial):
        self.serial = serial

    def read(self, size: int = 1) -> bytes:
        return self.serial.read(size)

    def write(self, data: bytes) -> int:
        return self.serial.write(data)

    def open(self):
        if self.isOpen():
            self.serial.open()

    def close(self):
        self.serial.close()

    def isOpen(self) -> bool:
        return self.serial.isOpen()
