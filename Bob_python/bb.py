from Bob.robot.concrete.crt_dynamixel import DynamixelRobot
from Bob.robot.concrete.crt_dynamixel_servo import RX_64

s1 = RX_64(1, 1)
s2 = RX_64(3, 1)
s3 = RX_64(5, 1)
s4 = RX_64(7, 1)
servos = {1: s1, 3: s2, 5: s3, 7: s4}

robot = DynamixelRobot('/dev/ttyUSB0', 115200)
robot.setVelocity(servos[1], 1023)
robot.setVelocity(servos[3], 1023)
robot.setVelocity(servos[5], 1023)
robot.setVelocity(servos[7], 1023)
robot.open()

robot.close()
