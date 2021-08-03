import abc
import csv

from typing import List

from robotics.framework.command import Command, CommandFactory


def empty(content: str):
    return content == ''


class Action(metaclass=abc.ABCMeta):
    def __init__(self):
        pass

    @abc.abstractmethod
    def getList(self) -> List[Command]:
        pass


class CSVAction(Action):
    def __init__(self, csv_file: str, factory: CommandFactory):
        super().__init__()
        self.strategy = factory
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
                        cmdList.append(self.strategy.create(id=int(_id), pos=int(position), speed=int(speed)))
                        pass
                    elif not empty(delay):
                        cmdList.append(self.strategy.create(sleep_duration=float(delay)))
                line = line + 1

            return cmdList
