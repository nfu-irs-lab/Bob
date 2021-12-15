import abc

from typing import List

from robot.framework.fw_command import Command


class Action(metaclass=abc.ABCMeta):
    def __init__(self):
        pass

    @abc.abstractmethod
    def getList(self) -> List[Command]:
        pass

