import abc

from Bob.device.framework.fw_device import Device
from Bob.robot.framework.fw_action import Action


class DynamixelServo(abc.ABC):

    @abc.abstractmethod
    def getId(self):
        pass

    @abc.abstractmethod
    def getProtocol(self):
        pass

    @abc.abstractmethod
    def getGoalPositionAddress(self):
        pass

    @abc.abstractmethod
    def getPresentPositionAddress(self):
        pass

    @abc.abstractmethod
    def getGoalVelocityAddress(self):
        pass

    @abc.abstractmethod
    def getTorqueEnableAddress(self):
        pass

    @abc.abstractmethod
    def getMovingAddress(self):
        pass
