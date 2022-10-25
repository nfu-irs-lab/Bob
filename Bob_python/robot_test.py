from device_config import getRobot
from command_utils import getCommandsFromFileName

robot = getRobot()
robot.open()

robot.enableAllServos(True)
robot.doCommands(getCommandsFromFileName("car.csv"))

robot.close()
