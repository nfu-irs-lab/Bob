import abc
from typing import List

from Bob.device.framework.fw_device import Device
from Bob.robot.framework.fw_command import Command


class Robot(Device, abc.ABC):
    def __init__(self):
        self._FlagStop = False

    @abc.abstractmethod
    def doCommand(self, cmd: Command):
        pass

    @abc.abstractmethod
    def enableAllServos(self, enable: bool):
        pass

    def stopAllAction(self):
        self._FlagStop = True

    def doCommands(self, commands: List[Command]):
        if self._FlagStop:
            self._FlagStop = False

        for cmd in commands:
            if self._FlagStop:
                return
            self.doCommand(cmd)
