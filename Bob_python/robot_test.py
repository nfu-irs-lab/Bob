from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor, VirtualDynamixelRobotAdaptor
from constants import getCommandsFromFileName

bt_description = ".*CP2102.*"
bot_description = ".*FT232R.*"


# agent = CSVServoAgent("servos.csv")
# dynamixel = Dynamixel(getSerialNameByDescription(bot_description), 115200)
# for servo in agent.getDefinedServos():
#     dynamixel.appendServo(servo)
# robot = DynamixelRobotAdaptor(dynamixel)
robot = VirtualDynamixelRobotAdaptor()
robot.open()
robot.init()
robot.doCommands(getCommandsFromFileName("attack.csv"))
robot.close()
