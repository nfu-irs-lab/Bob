from Bob.robot.framework.fw_servo import DynamixelServo


class H42_20_S300_R(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        self._servoId = servoId
        self._protocol = protocol

    def getId(self):
        return self._servoId

    def getProtocol(self):
        return self._protocol

    def getGoalPositionAddress(self):
        return 596

    def getPresentPositionAddress(self):
        return 611

    def getGoalVelocityAddress(self):
        return 600

    def getTorqueEnableAddress(self):
        return 562

    def getMovingAddress(self):
        return 610


class RX_64(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        self._servoId = servoId
        self._protocol = protocol

    def getId(self):
        return self._servoId

    def getProtocol(self):
        return self._protocol

    def getGoalPositionAddress(self):
        return 30

    def getPresentPositionAddress(self):
        return 36

    def getGoalVelocityAddress(self):
        return 32

    def getTorqueEnableAddress(self):
        return 24

    def getMovingAddress(self):
        return 46


class MX_106(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        self._servoId = servoId
        self._protocol = protocol

    def getId(self):
        return self._servoId

    def getProtocol(self):
        return self._protocol

    def getGoalPositionAddress(self):
        return 30

    def getPresentPositionAddress(self):
        return 36

    def getGoalVelocityAddress(self):
        return 32

    def getTorqueEnableAddress(self):
        return 24

    def getMovingAddress(self):
        return 46
