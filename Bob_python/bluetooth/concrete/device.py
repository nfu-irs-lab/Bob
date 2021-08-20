import serial

from bluetooth.concrete.monitor import ReadLineStrategy
from bluetooth.framework.device import BluetoothDevice
from bluetooth.framework.monitor import SerialMonitor, SerialListener
from bluetooth.framework.package import Package


class SerialBluetoothDevice(BluetoothDevice):
    def __init__(self, device, listener: SerialListener):
        self.ser = serial.Serial(device, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=1)
        self.monitor = SerialMonitor("monitor", self.ser, listener, ReadLineStrategy())
        self.monitor.start()

    def open(self):
        if not self.ser.is_open:
            self.ser.open()

    def write(self, package: Package):
        self.ser.write(package.getBytes())

    def close(self):
        self.ser.close()
        self.monitor.stop()
        pass

    def isOpen(self):
        return self.ser.isOpen()

    def interrupt(self):
        self.close()
