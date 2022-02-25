import time
from Bob.robot.concrete.crt_command import SleepCommand, DynamixelTorqueEnableCommand, \
    DynamixelVelocityCommand, DynamixelPositionCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.framework.fw_command import Command
from Bob.robot.framework.fw_robot import Robot


class DynamixelRobotAdaptor(Robot):

    def __init__(self, dynamixel: Dynamixel):
        super().__init__()
        self.dynamixel = dynamixel

    def open(self):
        self.dynamixel.open()

    def close(self):
        self.dynamixel.close()

    def isOpen(self) -> bool:
        return self.isOpen()

    def enableAllServos(self, enable: bool):
        for servo in self.dynamixel.servos:
            self.doCommand(DynamixelTorqueEnableCommand(servo.getId(), enable))

    def doCommand(self, cmd: Command):
        if type(cmd) == DynamixelVelocityCommand:
            self.dynamixel.setVelocity(cmd.servoId, cmd.velocity)
        elif type(cmd) == DynamixelPositionCommand:
            self.dynamixel.setGoalPosition(cmd.servoId, cmd.position)
        elif type(cmd) == DynamixelTorqueEnableCommand:
            self.dynamixel.enableTorque(cmd.servoId, cmd.enable)
        elif type(cmd) == SleepCommand:
            time.sleep(cmd.duration)

    def getAllServosId(self):
        return self.dynamixel.getAllServosId()

    def readPosition(self, servoId: int) -> int:
        return self.dynamixel.getPresentPosition(servoId)


class VirtualDynamixelRobotAdaptor(Robot):

    def __init__(self):
        super().__init__()
        self._is_open = False

    def open(self):
        print("Robot has been opened.")
        self._is_open = True

    def init(self):
        print("Robot has been initialized.")

    def close(self):
        self._is_open = False
        print("Robot has been closed.")

    def isOpen(self) -> bool:
        return self._is_open

    def enableAllServos(self, enable: bool):
        s = ""
        if enable:
            s = "Enable"
        else:
            s = "Disable"

        print(f"{s} all servos")

    def doCommand(self, cmd: Command):

        if type(cmd) == DynamixelVelocityCommand:
            print(f'ID: {cmd.servoId}\tVelocity: {cmd.velocity}')
        elif type(cmd) == DynamixelPositionCommand:
            print(f'ID: {cmd.servoId}\tPosition: {cmd.position}')
        elif type(cmd) == DynamixelTorqueEnableCommand:
            print(f'ID: {cmd.servoId}\tTorqueEnable: {cmd.enable}')
        elif type(cmd) == SleepCommand:
            print(f'Sleep: {cmd.duration}')
