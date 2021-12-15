import abc

from typing import List

from Bob.robot.framework.fw_command import Command


class Action(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def getList(self) -> List[Command]:
        pass
