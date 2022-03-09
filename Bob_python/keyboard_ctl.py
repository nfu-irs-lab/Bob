import keyboard  # using module keyboard

from Bob.robot.concrete.crt_command import *
from device_config import getRobot

robot = getRobot()
robot.open()
robot.enableAllServos(True)

w_lock = False
keyboard.on_press_key("w", lambda _: key_w_press())
keyboard.on_release_key("w", lambda _: key_w_release())

s_lock = False
keyboard.on_press_key("s", lambda _: key_s_press())
keyboard.on_release_key("s", lambda _: key_s_release())

d_lock = False
keyboard.on_press_key("d", lambda _: key_d_press())
keyboard.on_release_key("d", lambda _: key_d_release())

a_lock = False
keyboard.on_press_key("a", lambda _: key_a_press())
keyboard.on_release_key("a", lambda _: key_a_release())

keyboard.on_press_key(" ", lambda _: stop())


def stop():
    print("Stop")
    left_ctl(0)
    right_ctl(0)


def right_ctl(velocity: int):
    robot.doCommand(DynamixelVelocityCommand(12, velocity))
    robot.doCommand(DynamixelVelocityCommand(13, velocity))


def left_ctl(velocity: int):
    robot.doCommand(DynamixelVelocityCommand(11, velocity))
    robot.doCommand(DynamixelVelocityCommand(14, velocity))


def key_w_press():
    global w_lock
    if not w_lock:
        w_lock = True
        walk(1)


def key_w_release():
    global w_lock
    w_lock = False
    stop()


def key_s_press():
    global s_lock
    if not s_lock:
        s_lock = True
        walk(2)


def key_s_release():
    global s_lock
    s_lock = False
    stop()


def key_d_press():
    global d_lock
    if not d_lock:
        d_lock = True
        walk(3)


def key_d_release():
    global d_lock
    d_lock = False
    stop()


def key_a_press():
    global a_lock
    if not a_lock:
        a_lock = True
        walk(4)


def key_a_release():
    global a_lock
    a_lock = False
    stop()


def walk(direction: int):
    if direction == 1:
        right_ctl(-5000)
        left_ctl(5000)
        print("walk forward")
    elif direction == 2:
        right_ctl(5000)
        left_ctl(-5000)
        print("walk backward")
    elif direction == 3:
        right_ctl(-5000)
        left_ctl(-5000)
        print("turn right")
    elif direction == 4:
        right_ctl(5000)
        left_ctl(5000)
        print("turn left")


while True:
    pass
