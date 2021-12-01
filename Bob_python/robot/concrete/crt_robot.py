import time

from Bob.device.framework.fw_device import SerialDevice
from robot.framework.fw_command import Command
from robot.framework.fw_robot import Robot


class SerialRobot(Robot):
    def __init__(self, device: SerialDevice):
        super().__init__()
        self.serial = device

    def doCommand(self, cmd: Command):
        byteArray = cmd.getBytes()

        if byteArray is Command.SLEEP:
            time.sleep(cmd.sleep_duration)
        else:
            self.serial.write(byteArray)
            time.sleep(0.1)

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

    def doCommand(self, cmd: Command):
        string = "["
        for b in cmd.getBytes():
            string = string + str(b) + ","
        string = string + "]"
        print(string)


class PrintedRobot(Robot):

    def open(self):
        pass

    def isOpen(self) -> bool:
        return True

    def close(self):
        pass

    def __init__(self):
        super().__init__()

    def doCommand(self, cmd: Command):
        byteArray = cmd.getBytes()
        if byteArray == Command.SLEEP:
            print("Sleep:", cmd.sleep_duration)
        else:
            print(byteArray.decode())
