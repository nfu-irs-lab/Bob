import time

from Bob.robot.concrete.crt_command import DynamixelCommand
from Bob.robot.concrete.crt_dynamixel import Dynamixel, PROTOCOL_2
from Bob.robot.concrete.crt_robot import DynamixelRobotAdaptor
from Bob.robot.concrete.servo_agent import CSVServoAgent

agent = CSVServoAgent("servos.csv")
dynamixel = Dynamixel('/dev/ttyUSB0', 115200)
for servo in agent.getDefinedServos():
    dynamixel.appendServo(servo)

robot = DynamixelRobotAdaptor(dynamixel)
robot.open()
robot.init()
robot.doCommand(DynamixelCommand(1, 0, 10000))
time.sleep(3)
robot.doCommand(DynamixelCommand(1, 0, -10000))
time.sleep(3)
robot.doCommand(DynamixelCommand(1, 0, 0))

#
# dynamixel.open()
# for i in range(1, 11):
#     result = dynamixel.ping(i)
#     status = ""
#     if result:
#         status = "OK"
#     else:
#         status = "Fail"
#     print(f'ID#{i} {status}')
# dynamixel.close()
