import abc
import threading

from serial import Serial


class SerialReadStrategy(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def warp(self, data: bytes):
        pass

    @abc.abstractmethod
    def isIntegralPackage(self) -> bool:
        pass

    @abc.abstractmethod
    def getPackage(self) -> bytes:
        pass


class SerialListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def onReceive(self, data: bytes):
        pass


class SerialMonitor(threading.Thread):
    def __init__(self, threadName, ser: Serial, listener: SerialListener, strategy: SerialReadStrategy):
        super(SerialMonitor, self).__init__(name=threadName)
        self.listener = listener
        self.strategy = strategy
        self.ser = ser
        self.running = True

    def run(self):
        while self.running:
            try:
                data = self.ser.read(1024)
                self.strategy.warp(data)

                if self.strategy.isIntegralPackage():
                    self.listener.onReceive(self.strategy.getPackage())
            except Exception:
                pass
                # print(e.strerror)

    def stop(self):
        self.running = False
