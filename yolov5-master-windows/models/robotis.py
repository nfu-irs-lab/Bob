import time
import serial
import abc


class RoboticsSerial(serial.Serial):
    def __init__(self, device: str):
        super().__init__(device, 57142, timeout=0.5)
        # super().__init__()
        self.bytesize = serial.EIGHTBITS


class RobotUnitAction:

    def __init__(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None):
        if sleep_duration:
            self.sleep_duration = sleep_duration
            self.bytes_message = None
        elif id and pos and speed:
            arr = [0xff, 0xff, id, 0x07, 0x03, 0x1e, pos & 255, pos // 256, speed & 255, speed // 256]
            tt = 0xff - (sum(arr[2:9]) & 255)
            arr.append(tt)
            self.bytes_message = bytes(arr)
            self.sleep_duration = None
        else:
            raise Exception

    def doUnitAction(self, ser: RoboticsSerial):
        if self.sleep_duration is not None:
            print("sleep:", self.sleep_duration)
            time.sleep(self.sleep_duration)

        elif self.bytes_message is not None:
            print(self.bytes_message)
            # ser.write(self.bytes_message)
        else:
            raise Exception


class RobotAction(metaclass=abc.ABCMeta):
    def __init__(self,action_list:list):
        self.action_list = action_list

    @staticmethod
    def parseAction(string: str):
        if string == "knife":
            return KnifeAction()
        else:
            return None

    def doAction(self, ser: RoboticsSerial):
        for unit_action in self.action_list:
            unit_action.doUnitAction(ser)


class KnifeAction(RobotAction):
    def __init__(self):
        super().__init__(self.getActionList())

    @staticmethod
    def getActionList() -> list:
        action_list = []
        action_list.append(RobotUnitAction(1, 2000, 50))
        action_list.append(RobotUnitAction(6, 2030, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 1835, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))

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
        action_list.append(RobotUnitAction(6, 2030, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 512, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2048, 50))
        action_list.append(RobotUnitAction(7, 2048, 50))
        action_list.append(RobotUnitAction(3, 834, 50))
        action_list.append(RobotUnitAction(8, 1835, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=3))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 3150, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2870, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=0.75))
        action_list.append(RobotUnitAction(1, 981, 50))
        action_list.append(RobotUnitAction(6, 2924, 50))
        action_list.append(RobotUnitAction(2, 2400, 50))
        action_list.append(RobotUnitAction(7, 1647, 50))
        action_list.append(RobotUnitAction(3, 1835, 50))
        action_list.append(RobotUnitAction(8, 200, 50))
        action_list.append(RobotUnitAction(4, 350, 50))
        action_list.append(RobotUnitAction(9, 450, 50))
        action_list.append(RobotUnitAction(sleep_duration=1))

        return action_list