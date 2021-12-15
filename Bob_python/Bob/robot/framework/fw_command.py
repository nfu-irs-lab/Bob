import abc
from abc import ABC


def isNone(data):
    return data is None


def isNotNone(data):
    return data is not None


class Command(ABC):
    SLEEP = bytes([0x11, 0x22])

    def __init__(self, id: int = None, pos: int = None, speed: int = None,
                 sleep_duration: float = None):
        if isNotNone(sleep_duration):
            self.sleep_duration = sleep_duration
        elif isNotNone(id) & isNotNone(pos) & isNotNone(speed):
            self.id = id
            self.position = pos
            self.speed = speed
            self.sleep_duration = None
        else:
            raise Exception("format error")

    @abc.abstractmethod
    def getBytes(self) -> bytes:
        pass


class CommandFactory(ABC):

    @abc.abstractmethod
    def create(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None) -> Command:
        pass
