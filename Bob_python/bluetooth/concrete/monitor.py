import time
from queue import Queue

from bluetooth.framework.monitor import SerialReadStrategy, SerialListener


class ReadLineStrategy(SerialReadStrategy):
    def __init__(self):
        self.buffer = Queue()
        self.isIntegral = False
        self.package_data = bytearray()
        self.delay_timer = 0

    def warp(self, data: bytes):

        current_time = time.time()

        if current_time >= self.delay_timer:
            self.buffer.queue.clear()

        indexOfEOL = -1
        i = 0
        for b in data:
            if b == 10:
                indexOfEOL = i
                break
            i = i + 1

        self.isIntegral = indexOfEOL != -1

        if not self.isIntegral:
            self.package_data = None
            for b in data:
                self.buffer.put(b)
            self.delay_timer = current_time + 1500

        else:
            for i in range(indexOfEOL):
                self.buffer.put(data[i])

            self.package_data = bytearray(self.buffer.qsize())

            for i in range(0, len(self.package_data)):
                self.package_data[i] = self.buffer.get()

            if indexOfEOL + 1 < len(data):
                for i in range(indexOfEOL + 1, len(data)):
                    self.buffer.put(data[i])
                self.delay_timer = current_time + 1500

    def isIntegralPackage(self) -> bool:
        return self.isIntegral

    def getPackage(self) -> bytes:
        if self.isIntegral:
            return bytes(self.package_data)
        else:
            return bytes(0)


class PrintedSerialListener(SerialListener):
    def onReceive(self, data: bytes):
        print(data.decode())
        pass
