from Bob.robot.framework.fw_servo import DynamixelServo


class RX_64(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        self._servoId = servoId
        self._protocol = protocol

    def getId(self):
        return self._servoId

    def getProtocol(self):
        return self._protocol

    def getGoalPositionAddress(self):
        pass

    def getPresentPositionAddress(self):
        pass

    def getGoalVelocityAddress(self):
        pass

    def getTorqueEnableAddress(self):
        pass
