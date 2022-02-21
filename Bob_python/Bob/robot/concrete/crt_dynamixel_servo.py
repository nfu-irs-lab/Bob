from Bob.robot.framework.fw_servo import DynamixelServo


class H42_20_S300_R(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        super().__init__(servoId, protocol)

    def getGoalPositionAddressLength(self):
        return {'address': 596, 'length': 4}

    def getPresentPositionAddressLength(self):
        return {'address': 611, 'length': 4}

    def getGoalVelocityAddressLength(self):
        return {'address': 600, 'length': 4}

    def getTorqueEnableAddressLength(self):
        return {'address': 562, 'length': 1}

    def getMovingAddressLength(self):
        return {'address': 610, 'length': 1}


class RX_64(DynamixelServo):

    def __init__(self, servoId: int, protocol: int):
        super().__init__(servoId, protocol)

    def getGoalPositionAddressLength(self):
        return {'address': 30, 'length': 2}

    def getPresentPositionAddressLength(self):
        return {'address': 36, 'length': 2}

    def getGoalVelocityAddressLength(self):
        return {'address': 32, 'length': 2}

    def getTorqueEnableAddressLength(self):
        return {'address': 24, 'length': 1}

    def getMovingAddressLength(self):
        return {'address': 46, 'length': 1}


class MX_106(DynamixelServo):

    def getGoalPositionAddressLength(self):
        return {'address': 30, 'length': 2}

    def getPresentPositionAddressLength(self):
        return {'address': 36, 'length': 2}

    def getGoalVelocityAddressLength(self):
        return {'address': 32, 'length': 2}

    def getTorqueEnableAddressLength(self):
        return {'address': 24, 'length': 1}

    def getMovingAddressLength(self):
        return {'address': 46, 'length': 1}
