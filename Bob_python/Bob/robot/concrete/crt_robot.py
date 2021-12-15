import time

from Bob.device.framework.fw_device import SerialDevice
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
