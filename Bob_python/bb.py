import time

from Bob.robot.concrete.crt_dynamixel import DynamixelRobot, PROTOCOL_2
from Bob.robot.concrete.crt_dynamixel_servo import RX_64, H42_20_S300_R

robot = DynamixelRobot('/dev/ttyUSB0', 115200)
robot.open()
robot.appendServo(H42_20_S300_R(1, PROTOCOL_2))

robot.enableToque(1, True)
robot.setGoalPosition(1, 70000)
time.sleep(3)
robot.setGoalPosition(1, 0)
robot.close()
