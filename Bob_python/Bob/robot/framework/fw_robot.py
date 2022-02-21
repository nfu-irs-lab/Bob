import abc

from Bob.device.framework.fw_device import Device
from Bob.robot.framework.fw_action import Action
from Bob.robot.framework.fw_command import Command


class Robot(Device, abc.ABC):
    def __init__(self):
        self._FlagStop = False

    @abc.abstractmethod
    def doCommand(self, cmd: Command):
        pass

    def stopAllAction(self):
        self._FlagStop = True

    def doAction(self, action: Action):
        if self._FlagStop:
            self._FlagStop = False

        for cmd in action.getList():
            if self._FlagStop:
                return
            self.doCommand(cmd)
