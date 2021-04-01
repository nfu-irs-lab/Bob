import time

import serial
import abc


class RoboticsSerial(serial.Serial):
    def __init__(self, device: str):
        super().__init__(device, 57142, timeout=0.5)
        self.bytesize = serial.EIGHTBITS


class RobotUnitAction(metaclass=abc.ABCMeta):
    def __init__(self, id: int = -1, pos: int = -1, speed: int = -1,
                 sleep_duration: float = None):
        if sleep_duration:
            self.sleep_duration = sleep_duration
            self.bytes_message = None
        elif id>-1 and pos>-1 and speed>-1:

            arr = []
            arr.append(0xff)
            arr.append(0xff)
            arr.append(id)
            arr.append(0x07)
            arr.append(0x03)
            arr.append(0x1e)
            arr.append(pos & 255)
            arr.append(pos // 256)
            arr.append(speed & 255)
            arr.append(speed // 256)
            tt = 0xff - (sum(arr[2:9]) & 255)
            arr.append(tt)

            self.bytes_message = bytes(arr)
            self.sleep_duration = None
        else:
            raise Exception

    def doUnitAction(self, ser: serial.Serial):
        if self.sleep_duration is not None:
            print("Sleep:", self.sleep_duration)
            time.sleep(self.sleep_duration)

        elif self.bytes_message is not None:
            print("Send:", self.bytes_message)
            ser.write(self.bytes_message)
            time.sleep(0.1)
        else:
            raise Exception


class RobotAction:
    def __init__(self, action_list: list):
        self.action_list = action_list

    @staticmethod
    def parseAction(string: str):
        if string == "knife":
            return KnifeAction()
        elif string == "reset":
            return ResetAction()
        elif string == "car":
            return CarAction()
        elif string == "cake":
            return CakeAction()
        elif string=="bowl":
            return BowlAction()
        elif string=="bird":
            return BirdlAction()

        else:
            return None

    def doAction(self, ser: serial.Serial):
        for unit_action in self.action_list:
            unit_action.doUnitAction(ser)


class ResetAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        return action_list


class KnifeAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))

        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2460, 50))
        action_list.append(RobotUnitAction(7, 1700, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 509, 60))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 590, 60))
        action_list.append(RobotUnitAction(sleep_duration=2.5))
        action_list.append(RobotUnitAction(1, 3150, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2460, 50))
        action_list.append(RobotUnitAction(7, 1700, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 509, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 590, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))
        action_list.append(RobotUnitAction(1, 2882, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2460, 50))
        action_list.append(RobotUnitAction(7, 1700, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 509, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 590, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))
        action_list.append(RobotUnitAction(1, 3150, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2460, 50))
        action_list.append(RobotUnitAction(7, 1700, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 509, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 590, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))
        action_list.append(RobotUnitAction(1, 2882, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2460, 50))
        action_list.append(RobotUnitAction(7, 1700, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 509, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 590, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 60))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 60))
        action_list.append(RobotUnitAction(sleep_duration=3))
        return action_list

    # def doAction(self, ser: RoboticsSerial):
    #     for unit_action in self.action_list:
    #         unit_action.doUnitAction(ser)


class CarAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2300, 50))
        action_list.append(RobotUnitAction(7, 1800, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 3182, 50))
        action_list.append(RobotUnitAction(6, 1200, 50))
        action_list.append(RobotUnitAction(2, 2300, 50))
        action_list.append(RobotUnitAction(7, 1800, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2782, 50))
        action_list.append(RobotUnitAction(6, 800, 50))
        action_list.append(RobotUnitAction(2, 2300, 50))
        action_list.append(RobotUnitAction(7, 1800, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 205, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 3182, 50))
        action_list.append(RobotUnitAction(6, 1200, 50))
        action_list.append(RobotUnitAction(2, 2300, 50))
        action_list.append(RobotUnitAction(7, 1800, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2782, 50))
        action_list.append(RobotUnitAction(6, 800, 50))
        action_list.append(RobotUnitAction(2, 2300, 50))
        action_list.append(RobotUnitAction(7, 1800, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 205, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        return action_list


class CakeAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2483, 50))
        action_list.append(RobotUnitAction(7, 1692, 50))
        action_list.append(RobotUnitAction(3, 1194, 50))
        action_list.append(RobotUnitAction(8, 0, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 341, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2483, 50))
        action_list.append(RobotUnitAction(7, 1692, 50))
        action_list.append(RobotUnitAction(3, 1194, 50))
        action_list.append(RobotUnitAction(8, 0, 50))
        action_list.append(RobotUnitAction(4, 400, 50))
        action_list.append(RobotUnitAction(9, 341, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2483, 50))
        action_list.append(RobotUnitAction(7, 1692, 50))
        action_list.append(RobotUnitAction(3, 1194, 50))
        action_list.append(RobotUnitAction(8, 0, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 341, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2483, 50))
        action_list.append(RobotUnitAction(7, 1692, 50))
        action_list.append(RobotUnitAction(3, 1194, 50))
        action_list.append(RobotUnitAction(8, 0, 50))
        action_list.append(RobotUnitAction(4, 400, 50))
        action_list.append(RobotUnitAction(9, 341, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        return action_list


class CakeAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2483, 50))
        action_list.append(RobotUnitAction(7, 1692, 50))
        action_list.append(RobotUnitAction(3, 1194, 50))
        action_list.append(RobotUnitAction(8, 0, 50))
        action_list.append(RobotUnitAction(4, 512, 50))

class BowlAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=2))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2382, 50))
        action_list.append(RobotUnitAction(7, 1730, 50))
        action_list.append(RobotUnitAction(3, 1540, 50))
        action_list.append(RobotUnitAction(8, 1, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2982, 50))
        action_list.append(RobotUnitAction(6, 1000, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=2))
        return action_list


class BirdlAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 1247, 50))
        action_list.append(RobotUnitAction(7, 2908, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=2))
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=2))
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 1247, 50))
        action_list.append(RobotUnitAction(7, 2908, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=2))
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 1990, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 838, 50))
        action_list.append(RobotUnitAction(8, 208, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        return action_list