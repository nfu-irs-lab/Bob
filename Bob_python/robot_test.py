from Bob.robot.concrete.crt_dynamixel import Dynamixel
from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor
from Bob.robot.concrete.servo_agent import CSVServoAgent
from Bob.serial_config import getSerialNameByDescription
from constants import getCommandsFromFileName

bt_description = ".*CP2102.*"
bot_description = ".*FT232R.*"


agent = CSVServoAgent("servos.csv")
dynamixel = Dynamixel(getSerialNameByDescription(bot_description), 57600)
for servo in agent.getDefinedServos():
    dynamixel.appendServo(servo)
robot = DynamixelRobotAdaptor(dynamixel)
robot.open()
robot.init()
robot.doAction(getCommandsFromFileName("attack.csv"))
robot.close()
