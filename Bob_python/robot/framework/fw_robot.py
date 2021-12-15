import abc

from Bob.device.framework.fw_device import Device
from robot.framework.fw_action import Action
from robot.framework.fw_command import Command


class Robot(Device, abc.ABC):

    @abc.abstractmethod
    def doCommand(self, cmd: Command):
        pass

    def doAction(self, action: Action):
        for cmd in action.getList():
            self.doCommand(cmd)
