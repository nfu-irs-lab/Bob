import abc


class Command(metaclass=abc.ABCMeta):
    SLEEP = bytes([0x11, 0x22])

    def __init__(self, id: int = None, pos: int = None, speed: int = None,
                 sleep_duration: float = None):
        if sleep_duration:
            self.sleep_duration = sleep_duration
        elif id and pos and speed:
            self.id = id
            self.position = pos
            self.speed = speed
            self.sleep_duration = None
        else:
            raise Exception

    @abc.abstractmethod
    def getBytes(self) -> bytes:
        pass


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


class CommandFactory(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def create(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None) -> Command:
        pass


class PrintedCommandFactory(CommandFactory):

    def create(self, id: int = None, pos: int = None, speed: int = None, sleep_duration: float = None) -> Command:
        if sleep_duration:
            return PrintedCommand(sleep_duration=sleep_duration)
        elif id and pos and speed:
            return PrintedCommand(id=id, pos=pos, speed=speed)
        else:
            raise Exception
