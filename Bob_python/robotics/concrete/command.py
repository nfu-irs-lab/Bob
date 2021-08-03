from robotics.framework.command import CommandFactory, Command


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
        if sleep_duration:
            return RoboticsCommand(sleep_duration=sleep_duration)
        elif id and pos and speed:
            return RoboticsCommand(id=id, pos=pos, speed=speed)
        else:
            raise Exception
