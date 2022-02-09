import abc
import threading

from Bob.communication.framework.fw_listener import PackageListener
from Bob.communication.framework.fw_strategy import SerialReadStrategy


class PackageMonitor(threading.Thread, abc.ABC):
    def __init__(self, listener: PackageListener, strategy: SerialReadStrategy):
        super().__init__()
        self._listener = listener
        self._strategy = strategy

    @abc.abstractmethod
    def stop(self):
        pass
