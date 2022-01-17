from Bob.serial_config import getRobotWithDescription, getRobotWithName
from constants import getActionFromFileName

bt_description = ".*CP2102.*"
bot_description = ".*FT232R.*"

robot = getRobotWithDescription(bot_description)
# robot = getRobotWithName("/dev/ttyUSB0")
robot.doAction(getActionFromFileName("attack.csv"))
robot.close()
