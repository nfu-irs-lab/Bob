import re
from serial.tools.list_ports_linux import comports
from bluetooth.concrete.device import SerialBluetoothDevice
from bluetooth.framework.device import BluetoothDevice
from bluetooth.framework.monitor import SerialListener
from robotics.concrete.robot import RoboticsRobot
from robotics.framework.robot import Robot

bt_dev = ".*CP2102.*"
bot_dev = ".*FT232R.*"


def getBluetooth(listener: SerialListener) -> BluetoothDevice:
    for port in comports():
        if re.search(bt_dev, port.description):
            bt = SerialBluetoothDevice(port.device, listener)
            if not bt.isOpen():
                bt.open()
            return bt
    raise Exception(bt_dev + " not found.")


def getRobot() -> Robot:
    for port in comports():
        if re.search(bot_dev, port.description):
            bot = RoboticsRobot(port.device)
            if not bot.isOpen():
                bot.open()
            return bot

    raise Exception(bot_dev + " not found.")
