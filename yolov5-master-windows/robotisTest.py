from models.robotis import RoboticsSerial
from models.robotis import RobotAction

robot_serial = RoboticsSerial()
action = RobotAction.parseAction("knife")
action.doAction(robot_serial)

