from robot.framework.fw_command import CommandFactory, Command, isNotNone


def isNone(data):
    return data is None


def isNotNone(data):
    return data is not None


class RoboticsCommand(Command):

    def __init__(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None):
        super().__init__(id, pos, speed, sleep_duration)

    def getBytes(self) -> bytes:

        if self.sleep_duration is None:
            arr = []
            arr.append(0xff)
            arr.append(0xff)
            arr.append(self.id)
            arr.append(0x07)
            arr.append(0x03)
            arr.append(0x1e)
            arr.append(self.position & 255)
            arr.append(self.position // 256)
            arr.append(self.speed & 255)
            arr.append(self.speed // 256)
            arr.append(0xff - (sum(arr[2:9]) & 255))
            return bytes(arr)
        else:
            return self.SLEEP


class RoboticsCommandFactory(CommandFactory):

    def create(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None) -> Command:
        if isNotNone(sleep_duration):
            return RoboticsCommand(sleep_duration=sleep_duration)
        elif isNotNone(id) & isNotNone(pos) & isNotNone(speed):
            return RoboticsCommand(id=id, pos=pos, speed=speed)
        else:
            raise Exception("format error")


class PrintedCommand(Command):

    def __init__(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None):
        super().__init__(id, pos, speed, sleep_duration)

    def getBytes(self) -> bytes:
        if self.sleep_duration is None:
            string = "Do: id={id},pos={pos},speed={speed}".format(id=self.id, pos=self.position, speed=self.speed)
            return string.encode()
        else:
            string = "Sleep: {duration}".format(duration=self.sleep_duration)
            return string.encode()


class PrintedCommandFactory(CommandFactory):
    def create(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None) -> Command:
        if sleep_duration:
            return PrintedCommand(sleep_duration=sleep_duration)

        elif isNotNone(id) & isNotNone(pos) & isNotNone(speed):
            return PrintedCommand(id=id, pos=pos, speed=speed)
        else:
            raise Exception
