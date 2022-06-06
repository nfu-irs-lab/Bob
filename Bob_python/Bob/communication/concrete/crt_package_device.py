from Bob.communication.framework.fw_package_device import PackageDevice
from Bob.communication.framework.fw_strategy import SerialReadStrategy
from Bob.communication.codec import PackageCodec
from Bob.device.framework.fw_device import SerialDevice


class SerialPackageDevice(PackageDevice):

    def __init__(self, ser: SerialDevice, strategy: SerialReadStrategy):
        super().__init__()
        self.__strategy = strategy
        self.__ser = ser
        self.__running = True

    def open(self):
        if not self.__ser.isOpen():
            self.__ser.open()

    def close(self):
        self.__ser.close()
        self.__running = False

    def isOpen(self):
        return self.__ser.isOpen()

    def writeString(self, string: str) -> int:
        return self.__ser.write(PackageCodec.encodeString(string))

    def write(self, b: bytes) -> int:
        return self.__ser.write(PackageCodec.encode(b))

    def run(self):
        while self.__running:
            try:
                data = self.__ser.read(1024)

                if len(data) == 0:
                    continue

                self.__strategy.warp(data)

                while self.__strategy.hasNextPackage():
                    self._onReceive(self.__strategy.nextPackage())
            except KeyboardInterrupt:
                self.stop()

    def stop(self):
        self.__running = False
