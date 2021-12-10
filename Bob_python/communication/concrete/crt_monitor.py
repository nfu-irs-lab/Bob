from typing import List

from Bob.device.framework.fw_device import SerialDevice
from communication.framework.fw_monitor import SerialReadStrategy, SerialListener, PackageMonitor
from communication.framework.fw_package_device import PackageDevice


class ReadLineStrategy(SerialReadStrategy):
    def __init__(self):
        # self.waiting_time = 0
        self.buffer = bytearray()
        self.delay_timer = 0
        self.packages: List[bytes] = []

    def warp(self, data: bytes):
        for b in data:
            self.buffer.append(b)

        indexOfFirstEOL = self.__getIndexOfFirstEOL(self.buffer)
        while indexOfFirstEOL != -1:
            # remove \n,add to packages array.
            self.packages.append(self.buffer[0:indexOfFirstEOL])

            # remove from content to \n in buffer.
            del self.buffer[0:indexOfFirstEOL + 1]
            indexOfFirstEOL = self.__getIndexOfFirstEOL(self.buffer)

    def hasNextPackage(self) -> bool:
        return len(self.packages) > 0

    def nextPackage(self) -> bytes:
        if self.hasNextPackage():
            b = self.packages[0]
            del self.packages[0]
            return b
        else:
            raise RuntimeError("No package")

    def __getIndexOfFirstEOL(self, data):
        indexOfEOL = -1
        i = 0
        for b in data:
            if b == 10:
                indexOfEOL = i
                break
            i = i + 1

        return indexOfEOL


class PrintedSerialListener(SerialListener):
    def onReceive(self, device: PackageDevice, data: bytes):
        print(data.decode())
        pass


class SerialPackageMonitor(PackageMonitor):
    def __init__(self, device: PackageDevice, ser: SerialDevice, listener: SerialListener,
                 strategy: SerialReadStrategy):
        super().__init__(device, listener, strategy)
        self._ser = ser
        self._running = True

    def run(self):
        while self._running:
            try:
                data = self._ser.read(1024)

                if len(data) == 0:
                    continue

                self._strategy.warp(data)

                while self._strategy.hasNextPackage():
                    self._listener.onReceive(self._package_device, self._strategy.nextPackage())
            except KeyboardInterrupt:
                self.stop()
            except Exception as e:
                print(e.strerror)

    def stop(self):
        self._running = False
