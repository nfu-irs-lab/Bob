import abc


class Package(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def getBytes(self) -> bytes:
        pass
