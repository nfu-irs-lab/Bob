import time

from Bob.robot.concrete.crt_dynamixel import DynamixelRobot
from Bob.robot.concrete.crt_dynamixel_servo import RX_64, H42_20_S300_R

s1 = H42_20_S300_R(1, 2)
s2 = RX_64(2, 1)
robot = DynamixelRobot('/dev/ttyUSB0', 115200)
robot.open()
robot.enableToque(s1,True)
robot.enableToque(s2,True)
robot.setGoalPosition(s1, 0)
robot.setGoalPosition(s2, 0)
time.sleep(3)
robot.setVelocity(s1,70000)
robot.setGoalPosition(s1,70000)
robot.setGoalPosition(s2, 1023)
robot.close()
