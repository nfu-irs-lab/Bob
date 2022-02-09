from Bob.communication.framework.fw_listener import PackageListener
from Bob.communication.framework.fw_monitor import PackageMonitor
from Bob.communication.framework.fw_strategy import SerialReadStrategy
from Bob.device.framework.fw_device import SerialDevice


class SerialPackageMonitor(PackageMonitor):
    def __init__(self, ser: SerialDevice, listener: PackageListener,
                 strategy: SerialReadStrategy):
        super().__init__(listener, strategy)
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
                    self._listener.onReceive(self._strategy.nextPackage())
            except KeyboardInterrupt:
                self.stop()
            except Exception as e:
                print(e.__str__())

    def stop(self):
        self._running = False
