from typing import List

from Bob.robot.concrete.crt_command import DynamixelTorqueEnableCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor, VirtualDynamixelRobotAdaptor
from Bob.robot.concrete.servo_agent import CSVServoAgent
from Bob.serial_config import getSerialNameByDescription
from constants import getCommandsFromFileName

bt_description = ".*CP2102.*"
bot_description = ".*FT232R.*"

agent = CSVServoAgent("servos.csv")
dynamixel = Dynamixel(getSerialNameByDescription(bot_description), 115200)
for servo in agent.getDefinedServos():
    dynamixel.appendServo(servo)
robot = DynamixelRobotAdaptor(dynamixel)
# robot = VirtualDynamixelRobotAdaptor()
robot.open()
for _id in robot.getAllServosId():
    robot.doCommand(DynamixelTorqueEnableCommand(_id, True))

robot.doCommands(getCommandsFromFileName("test.csv"))

for _id in robot.getAllServosId():
    robot.doCommand(DynamixelTorqueEnableCommand(_id, False))
robot.close()
