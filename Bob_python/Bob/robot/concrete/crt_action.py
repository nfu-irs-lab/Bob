import csv
from typing import List

from Bob.robot.concrete.crt_command import DynamixelCommand
from Bob.robot.framework.fw_action import Action
from Bob.robot.framework.fw_command import Command, SleepCommand


def empty(content: str):
    return content == ''


class CSVAction(Action):
    def __init__(self, csv_file: str):
        super().__init__()
        self.csv_file = csv_file

    def getList(self) -> List[Command]:
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
