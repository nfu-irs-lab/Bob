from models.robotis import RobotAction, ResetAction
from models.robotis import RoboticsSerial

ser = RoboticsSerial('COM8')

# robot_serial = RoboticsSerial("COM8")
action = RobotAction.parseAction("cake")
action.doAction(ser)
ResetAction().doAction(ser)
ser.close()