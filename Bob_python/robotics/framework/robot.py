import abc
import time

from serial import Serial
from robotics.framework.action import Action
from robotics.framework.command import Command


class Robot(metaclass=abc.ABCMeta):
    def __init__(self):
        pass

    @abc.abstractmethod
    def doCommand(self, cmd: Command):
        pass

    @abc.abstractmethod
    def close(self):
        pass

    def doAction(self, action: Action):
        for cmd in action.getList():
            self.doCommand(cmd)


class PrintedRobot(Robot):

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


class BytePrintedRobot(Robot):

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


class SerialRobot(Robot):

    def __init__(self, ser: Serial):
        super().__init__()
        self.serial = ser

    def doCommand(self, cmd: Command):
        byteArray = cmd.getBytes()

        if byteArray is Command.SLEEP:
            time.sleep(cmd.sleep_duration)
        else:
            self.serial.write(byteArray)
            time.sleep(0.1)

    def close(self):
        self.serial.close()
