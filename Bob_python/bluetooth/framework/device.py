import abc

from bluetooth.framework.package import Package


class BluetoothDevice(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def open(self):
        pass

    @abc.abstractmethod
    def write(self, package: Package):
        pass

    @abc.abstractmethod
    def close(self):
        pass
