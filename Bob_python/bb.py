import time

from Bob.robot.concrete.crt_dynamixel import DynamixelRobot, PROTOCOL_2
from Bob.robot.concrete.servo_agent import CSVServoAgent

agent = CSVServoAgent("servos.csv")
robot = DynamixelRobot('/dev/ttyUSB0', 115200)
for servo in agent.getDefinedServos():
    robot.appendServo(servo)

robot.open()
robot.enableToque(1, True)

robot.setVelocity(3, 100)
robot.setVelocity(2, 100)
robot.setVelocity(1, 7000)

robot.setGoalPosition(1, 70000)
robot.setGoalPosition(2, 1023)
robot.setGoalPosition(3, 1023)
time.sleep(3)
robot.setGoalPosition(1, 0)
robot.setGoalPosition(2, 0)
robot.setGoalPosition(3, 0)
robot.close()
