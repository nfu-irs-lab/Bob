import abc
import time
from abc import ABC
from typing import Optional


class Command(ABC):

    @abc.abstractmethod
    def doCommand(self) -> Optional:
        pass


class BytesCommand(Command, ABC):

    def doCommand(self) -> Optional:
        return self.getBytes()

    @abc.abstractmethod
    def getBytes(self) -> bytes:
        pass


class SleepCommand(Command):
    def __init__(self, duration: float):
        self.__duration = duration

    def doCommand(self):
        time.sleep(self.__duration)
        print("Sleep:%.2f" % self.__duration)
        return None
