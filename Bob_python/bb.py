import time

from Bob.robot.concrete.crt_dynamixel import DynamixelRobot, PROTOCOL_2
from Bob.robot.concrete.servo_agent import CSVServoAgent

agent = CSVServoAgent("servos.csv")
robot = DynamixelRobot('/dev/ttyUSB0', 57600)
for servo in agent.getDefinedServos():
    robot.appendServo(servo)

robot.open()
for i in range(1, 11):
    result = robot.ping(i)
    status = ""
    if result:
        status = "OK"
    else:
        status = "Fail"
    print(f'ID#{i} {status}')
robot.close()
