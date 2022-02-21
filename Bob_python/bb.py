import time

from Bob.robot.concrete.crt_dynamixel import DynamixelRobot, PROTOCOL_2
from Bob.robot.concrete.servo_agent import CSVServoAgent

agent = CSVServoAgent("servos.csv")
robot = DynamixelRobot('/dev/ttyUSB0', 115200)
for servo in agent.getDefinedServos():
    robot.appendServo(servo)

robot.open()

# robot.enableToque(1, True)
# robot.setVelocity(1, 5000)
# robot.setGoalPosition(1, 70000)
# time.sleep(3)
# robot.setGoalPosition(1, 3500)
# print(robot.isMoving(1))
# time.sleep(3)
# print(robot.getPresentPosition(1))
# robot.enableToque(1, False)
# print(robot.isMoving(1))

robot.setGoalPosition(2,1023)
time.sleep(3)
robot.setGoalPosition(2,0)

robot.close()
