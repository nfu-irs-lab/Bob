import keyboard  # using module keyboard

from Bob.robot.concrete.crt_command import DynamixelCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor
from Bob.robot.concrete.servo_agent import CSVServoAgent

agent = CSVServoAgent("servos.csv")
dynamixel = Dynamixel('/dev/ttyUSB0', 115200)
for servo in agent.getDefinedServos():
    dynamixel.appendServo(servo)

robot = DynamixelRobotAdaptor(dynamixel)
robot.open()
robot.init()

while True:
    i = input()
    if i == "w":
        print("walk forward")
        robot.doCommand(DynamixelCommand(1, 0, 10000))
    if i == "s":
        print("walk backward")
        robot.doCommand(DynamixelCommand(1, 0, -10000))
    if i == "d":
        print("turn right")
    if i == "a":
        print("turn left")
    if i == "q":
        break

robot.doCommand(DynamixelCommand(1, 0, 0))
robot.close()
