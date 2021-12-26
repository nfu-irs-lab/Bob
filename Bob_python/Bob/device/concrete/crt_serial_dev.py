from serial import Serial
from Bob.device.framework.fw_device import SerialDevice


class LocalSerialDevice(SerialDevice):

    def __init__(self, serial: Serial, write_delay_ms: int):
        super().__init__(write_delay_ms)
        self.serial = serial

    def read(self, size: int = 1) -> bytes:
        return self.serial.read(size)

    def _write_without_delay(self, data: bytes) -> int:
        return self.serial.write(data)

    def open(self):
        if self.isOpen():
            self.serial.open()

    def close(self):
        self.serial.close()

    def isOpen(self) -> bool:
        return self.serial.isOpen()


class BluetoothSocketSerialDevice(SerialDevice):

    def __init__(self, client_sock, write_delay_ms: int):
        super().__init__(write_delay_ms)
        self.client_sock = client_sock

    def read(self, n: int) -> bytes:
        return self.client_sock.recv(n)

    def _write_without_delay(self, data: bytes) -> int:
        return self.client_sock.send(data)

    def open(self):
        pass

    def close(self):
        self.client_sock.close()

    def isOpen(self) -> bool:
        return True
