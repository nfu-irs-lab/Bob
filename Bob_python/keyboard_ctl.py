import keyboard  # using module keyboard

from Bob.robot.concrete.crt_command import *
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor
from Bob.robot.concrete.servo_agent import CSVServoAgent
from Bob.serial_config import getSerialNameByDescription

bot_description = ".*FT232R.*"
agent = CSVServoAgent("servos.csv")
dynamixel = Dynamixel(getSerialNameByDescription(bot_description), 115200)
for servo in agent.getDefinedServos():
    dynamixel.appendServo(servo)

robot = DynamixelRobotAdaptor(dynamixel)
robot.open()

robot.doCommand(DynamixelTorqueEnableCommand(11, True))
robot.doCommand(DynamixelTorqueEnableCommand(12, True))
keyboard.on_press_key("w", lambda _: walk(1))
keyboard.on_press_key("s", lambda _: walk(2))
keyboard.on_press_key("d", lambda _: walk(3))
keyboard.on_press_key("a", lambda _: walk(4))


def walk(direction: int):
    if direction == 1:
        robot.doCommand(DynamixelVelocityCommand(11, -10000))
        robot.doCommand(DynamixelVelocityCommand(12, 10000))
        print("walk forward")
    elif direction == 2:
        robot.doCommand(DynamixelVelocityCommand(11, 10000))
        robot.doCommand(DynamixelVelocityCommand(12, -10000))
        print("walk backward")
    elif direction == 3:
        print("turn right")
    elif direction == 4:
        print("turn left")


while True:
    pass
