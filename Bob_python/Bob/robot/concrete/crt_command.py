from Bob.robot.framework.fw_command import BytesCommand


class RoboticsBytesCommand(BytesCommand):

    def __init__(self, id: int, position: int, speed: int):
        self.speed = speed
        self.position = position
        self.id = id

    def getBytes(self) -> bytes:
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
