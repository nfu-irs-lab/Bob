import time

from Bob.device.framework.fw_device import SerialDevice
from Bob.robot.concrete.crt_command import DynamixelCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.framework.fw_command import BytesCommand, Command
from Bob.robot.framework.fw_robot import Robot


class SerialRobot(Robot):
    def __init__(self, device: SerialDevice):
        super().__init__()
        self.serial = device

    def doCommand(self, cmd: Command):
        do = cmd.doCommand()
        if do is not None:
            self.serial.write(do)

    def isOpen(self):
        return self.serial.isOpen()

    def open(self):
        self.serial.open()

    def close(self):
        self.serial.close()
        pass


class BytePrintedRobot(Robot):

    def open(self):
        pass

    def isOpen(self) -> bool:
        return True

    def close(self):
        pass

    def __init__(self):
        super().__init__()

    def doCommand(self, cmd: BytesCommand):
        do = cmd.doCommand()
        if do is not None:
            string = "["
            for b in cmd.getBytes():
                string = string + str(b) + ","
            string = string + "]"
            print(string)


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
            self.dynamixel.setVelocity(cmd.id, cmd.speed)
            self.dynamixel.setGoalPosition(cmd.id, cmd.position)
        else:
            cmd.doCommand()
