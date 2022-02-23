import csv
from typing import Optional, List

from Bob.robot.framework.fw_command import Command, CommandFactory


def empty(content: str):
    return content == ''


class CSVCommandFactory(CommandFactory):
    def __init__(self, csv_file: str):
        self.csv_file = csv_file

    def create(self) -> Optional[Command]:
        raise Exception("This method can't be used.")

    def createList(self) -> Optional[List[Command]]:
        with open(self.csv_file, newline='') as file:
            cmdList = []
            rows = csv.reader(file, delimiter=",")
            line = 0
            for row in rows:
                if line == 0:
                    pass
                else:
                    if len(row) == 0:
                        continue

                    servoId = row[0]
                    position = row[1]
                    speed = row[2]
                    delay = row[3]

                    if not empty(delay):
                        cmdList.append(SleepCommand(float(delay)))

                    if empty(servoId):
                        continue

                    if not empty(speed):
                        cmdList.append(DynamixelVelocityCommand(int(servoId), int(speed)))

                    if not empty(position):
                        cmdList.append(DynamixelPositionCommand(int(servoId), int(position)))
                line = line + 1
            return cmdList


class SleepCommand(Command):
    def __init__(self, duration: float):
        self.duration = duration


class DynamixelCommand(Command):

    def __init__(self, servoId: int):
        self.servoId = servoId


class DynamixelPositionCommand(DynamixelCommand):
    def __init__(self, servoId: int, position: int):
        super().__init__(servoId)
        self.position = position


class DynamixelVelocityCommand(DynamixelCommand):
    def __init__(self, servoId: int, velocity: int):
        super().__init__(servoId)
        self.velocity = velocity


class DynamixelTorqueEnableCommand(DynamixelCommand):

    def __init__(self, servoId: int, enable: bool):
        super().__init__(servoId)
        self.enable = enable
