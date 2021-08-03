import time

import serial

from robotics.framework.command import Command
from robotics.framework.robot import SerialRobot


class RoboticsRobot(SerialRobot):

    def __init__(self, device: str):
        ser = serial.Serial(device, baudrate=57142, timeout=0.5, write_timeout=1)

        super().__init__(ser)

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
