import abc
import time
from abc import ABC
from typing import Optional, List


class Command(ABC):
    pass


class CommandFactory(ABC):

    @abc.abstractmethod
    def create(self) -> Optional[Command]:
        return None

    @abc.abstractmethod
    def createList(self) -> Optional[List[Command]]:
        pass

