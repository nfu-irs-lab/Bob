import csv
from typing import Optional, List

from Bob.robot.framework.fw_command import BytesCommand, Command, CommandFactory, SleepCommand


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
                    _id = row[0]
                    position = row[1]
                    speed = row[2]
                    delay = row[3]

                    if empty(delay) and (not empty(_id)) and (not empty(position)) and (not empty(speed)):
                        cmdList.append(DynamixelCommand(id=int(_id), position=int(position), speed=int(speed)))
                    elif not empty(delay):
                        cmdList.append(SleepCommand(float(delay)))
                line = line + 1
            return cmdList


class DynamixelCommand(Command):

    def __init__(self, id: int, position: int, speed: int):
        self.speed = speed
        self.position = position
        self.id = id

    def doCommand(self) -> Optional:
        return self.id, self.position, self.speed
