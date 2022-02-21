import abc

from Bob.device.framework.fw_device import Device
from Bob.robot.framework.fw_action import Action


class DynamixelServo(abc.ABC):

    def __init__(self, servoId: int, protocol: int):
        self._servoId = servoId
        self._protocol = protocol

    def getId(self):
        return self._servoId

    def getProtocol(self):
        return self._protocol

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
