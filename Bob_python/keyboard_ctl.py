import keyboard  # using module keyboard

from Bob.robot.concrete.crt_command import *
from device_config import getRobot

robot = getRobot()
robot.open()
robot.enableAllServos(True)
keyboard.on_press_key("w", lambda _: walk(1))
keyboard.on_press_key("s", lambda _: walk(2))
keyboard.on_press_key("d", lambda _: walk(3))
keyboard.on_press_key("a", lambda _: walk(4))
keyboard.on_press_key(" ", lambda _: stop())
keyboard.on_press_key("q", lambda _: exit(1))


def stop():
    robot.doCommand(DynamixelVelocityCommand(11, 0))
    robot.doCommand(DynamixelVelocityCommand(12, 0))

    robot.doCommand(DynamixelVelocityCommand(13, 0))
    robot.doCommand(DynamixelVelocityCommand(14, 0))


def walk(direction: int):
    if direction == 1:
        robot.doCommand(DynamixelVelocityCommand(11, 10000))
        robot.doCommand(DynamixelVelocityCommand(12, 10000))

        robot.doCommand(DynamixelVelocityCommand(13, -10000))
        robot.doCommand(DynamixelVelocityCommand(14, -10000))
        print("walk forward")
    elif direction == 2:
        robot.doCommand(DynamixelVelocityCommand(11, -10000))
        robot.doCommand(DynamixelVelocityCommand(12, -10000))

        robot.doCommand(DynamixelVelocityCommand(13, 10000))
        robot.doCommand(DynamixelVelocityCommand(14, 10000))
        print("walk backward")
    elif direction == 3:
        robot.doCommand(DynamixelVelocityCommand(11, 10000))
        robot.doCommand(DynamixelVelocityCommand(12, 10000))

        robot.doCommand(DynamixelVelocityCommand(13, -2000))
        robot.doCommand(DynamixelVelocityCommand(14, -2000))
        print("turn right")
    elif direction == 4:
        robot.doCommand(DynamixelVelocityCommand(11, 2000))
        robot.doCommand(DynamixelVelocityCommand(12, 2000))

        robot.doCommand(DynamixelVelocityCommand(13, -10000))
        robot.doCommand(DynamixelVelocityCommand(14, -10000))
        print("turn left")


while True:
    pass
