from typing import Optional

from Bob.robot.framework.fw_command import BytesCommand, Command


class DynamixelCommand(Command):

    def __init__(self, id: int, position: int, speed: int):
        self.speed = speed
        self.position = position
        self.id = id

    def doCommand(self) -> Optional:
        return self.id, self.position, self.speed