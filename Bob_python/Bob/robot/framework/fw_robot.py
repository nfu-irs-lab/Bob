import abc

from Bob.device.framework.fw_device import Device
from Bob.robot.framework.fw_action import Action


class Robot(Device, abc.ABC):

    @abc.abstractmethod
    def moveToTargetPosition(self, id: int, position: int, velocity: int):
        pass
