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
    def getGoalPositionAddressLength(self):
        pass

    @abc.abstractmethod
    def getPresentPositionAddressLength(self):
        pass

    @abc.abstractmethod
    def getGoalVelocityAddressLength(self):
        pass

    @abc.abstractmethod
    def getTorqueEnableAddressLength(self):
        pass

    @abc.abstractmethod
    def getMovingAddressLength(self):
        pass

    # @abc.abstractmethod
    # def getToqueLimitAddress(self):
    #     pass
