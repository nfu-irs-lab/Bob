import abc


class PackageListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def onReceive(self, data: bytes):
        pass
