import csv
from typing import List

from Bob.robot.concrete.crt_dynamixel_servo import *


class CSVServoAgent:
    def __init__(self, csv_file: str):
        super().__init__()
        self.csv_file = csv_file

    def getDefinedServos(self) -> List[DynamixelServo]:
        with open(self.csv_file, newline='') as file:
            servoList: List[DynamixelServo] = []
            rows = csv.reader(file, delimiter=",")
            line = 0
            for row in rows:
                if line == 0:
                    pass
                else:
                    _id = int(row[0])
                    protocol = self._getProtocol(row[1])
                    model = row[2]
                    servoList.append(self._getModel(model, _id, protocol))
                line = line + 1

            return servoList

    def _getProtocol(self, string: str):
        if string == "1.0":
            return 1
        elif string == "2.0":
            return 2
        else:
            raise Exception("Unknown protocol")

    def _getModel(self, model: str, _id: int, protocol: int) -> DynamixelServo:
        if model == "RX-64":
            return RX_64(_id, protocol)
        elif model == "H42-20-S300-R":
            return H42_20_S300_R(_id, protocol)
        else:
            raise Exception("Unknown model")
