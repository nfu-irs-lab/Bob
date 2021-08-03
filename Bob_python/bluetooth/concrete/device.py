import serial

from bluetooth.framework.device import BluetoothDevice
from bluetooth.framework.package import Package


class SerialBluetoothDevice(BluetoothDevice):
    def __init__(self, device):
        self.ser = serial.Serial(device, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=1)

    def open(self):
        if not self.ser.is_open:
            self.ser.open()

    def write(self, package: Package):
        self.ser.write(package.getBytes())

    def close(self):
        self.ser.close()
        pass

    def isOpen(self):
        return self.ser.isOpen()
