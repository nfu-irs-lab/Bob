import time

from Bob.robot.concrete.crt_command import DynamixelCommand, SleepCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.framework.fw_command import Command
from Bob.robot.framework.fw_robot import Robot


class DynamixelRobotAdaptor(Robot):

    def __init__(self, dynamixel: Dynamixel):
        super().__init__()
        self.dynamixel = dynamixel

    def open(self):
        self.dynamixel.open()

    def init(self):
        for servo in self.dynamixel.servos:
            self.dynamixel.enableToque(servo.getId(), True)

    def close(self):
        self.dynamixel.close()

    def isOpen(self) -> bool:
        return self.isOpen()

    def doCommand(self, cmd: Command):
        if type(cmd) == DynamixelCommand:
            self.dynamixel.setVelocity(cmd.servoId, cmd.speed)
            self.dynamixel.setGoalPosition(cmd.servoId, cmd.position)
        elif type(cmd) == SleepCommand:
            time.sleep(cmd.duration)


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

    def doCommand(self, cmd: Command):
        if type(cmd) == DynamixelCommand:
            print(f'ID: {cmd.servoId}\tVelocity: {cmd.speed}\tPosition: {cmd.position}')
        elif type(cmd) == SleepCommand:
            print(f'Sleep: {cmd.duration}')
