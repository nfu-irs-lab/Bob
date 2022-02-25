import time

from command_utils import getCommandsFromFileName
from device_config import getRobot
import keyboard  # using module keyboard


def reset():
    robot.doCommands(getCommandsFromFileName("reset.csv"))


robot = getRobot()
robot.open()
print("Reset.....")
reset()
print("Unlock")
robot.enableAllServos(False)

keyboard.on_press_key("j", lambda _: lock())
keyboard.on_press_key("k", lambda _: unlock())
keyboard.on_press_key("i", lambda _: reset())

keyboard.on_press_key("l", lambda _: readPosition())


def unlock():
    print("Unlock")
    robot.enableAllServos(False)


def lock():
    print("Lock")
    robot.enableAllServos(True)


def readPosition():
    for _id in robot.getAllServosId():
        print(f'ID#{_id}\tPosition:{robot.readPosition(_id)}')


try:
    while True:
        time.sleep(0.05)
except KeyboardInterrupt:
    pass

robot.close()
