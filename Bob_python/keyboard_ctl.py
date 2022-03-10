import keyboard  # using module keyboard

from Bob.robot.concrete.crt_command import *
from Bob.robot.framework.fw_robot import Robot
from command_utils import getCommandsFromFileName
from device_config import getRobot

VOL = 4000


class KeyboardController:
    def __init__(self, robot: Robot):
        self.robot = robot
        if not robot.isOpen():
            robot.open()
        robot.enableAllServos(True)
        self._w_lock = False
        self._s_lock = False
        self._d_lock = False
        self._a_lock = False
        self._j_lock = False
        self._k_lock = False

    def init(self):
        keyboard.on_press_key("w", lambda _: self.key_w_press())
        keyboard.on_release_key("w", lambda _: self.key_w_release())
        keyboard.on_press_key("s", lambda _: self.key_s_press())
        keyboard.on_release_key("s", lambda _: self.key_s_release())

        keyboard.on_press_key("d", lambda _: self.key_d_press())
        keyboard.on_release_key("d", lambda _: self.key_d_release())
        keyboard.on_press_key("a", lambda _: self.key_a_press())
        keyboard.on_release_key("a", lambda _: self.key_a_release())

        keyboard.on_press_key("j", lambda _: self.key_j_press())
        keyboard.on_release_key("j", lambda _: self.key_j_release())

        keyboard.on_press_key("k", lambda _: self.key_k_press())
        keyboard.on_release_key("k", lambda _: self.key_k_release())
        keyboard.on_press_key(" ", lambda _: self.stop())

    def stop(self):
        print("Stop")
        self.left_ctl(0)
        self.right_ctl(0)

    def right_ctl(self, velocity: int):
        self.robot.doCommand(DynamixelVelocityCommand(12, velocity))
        self.robot.doCommand(DynamixelVelocityCommand(13, velocity))

    def left_ctl(self, velocity: int):
        self.robot.doCommand(DynamixelVelocityCommand(11, velocity))
        self.robot.doCommand(DynamixelVelocityCommand(14, velocity))

    def key_w_press(self):
        if not self._w_lock:
            self._w_lock = True
            self.walk(1)

    def key_w_release(self):
        self._w_lock = False
        self.stop()

    def key_s_press(self):
        if not self._s_lock:
            self._s_lock = True
            self.walk(2)

    def key_s_release(self):
        self._s_lock = False
        self.stop()

    def key_d_press(self):
        if not self._d_lock:
            self._d_lock = True
            self.walk(3)

    def key_d_release(self):
        self._d_lock = False
        self.stop()

    def key_a_press(self):
        if not self._a_lock:
            self._a_lock = True
            self.walk(4)

    def key_a_release(self):
        self._a_lock = False
        self.stop()

    def key_j_press(self):
        if not self._j_lock:
            self._j_lock = True
            self.robot.doCommands(getCommandsFromFileName("correct.csv"))

    def key_j_release(self):
        self._j_lock = False
        self.stop()

    def key_k_press(self):
        if not self._k_lock:
            self._k_lock = True
            self.robot.doCommands(getCommandsFromFileName("incorrect.csv"))

    def key_k_release(self):
        self._k_lock = False
        self.stop()

    def walk(self, direction: int):
        if direction == 1:
            self.right_ctl(-VOL)
            self.left_ctl(VOL)
            print("walk forward")
        elif direction == 2:
            self.right_ctl(VOL)
            self.left_ctl(-VOL)
            print("walk backward")
        elif direction == 3:
            self.right_ctl(VOL)
            self.left_ctl(VOL)
            print("turn right")
        elif direction == 4:
            self.right_ctl(-VOL)
            self.left_ctl(-VOL)
            print("turn left")


if __name__ == '__main__':
    ctl = KeyboardController(getRobot())
    ctl.init()
    while True:
        pass
