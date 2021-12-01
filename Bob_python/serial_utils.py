import re

from serial import Serial
from serial.tools.list_ports_linux import comports

from Bob.device.concrete.crt_serial_dev import LocalSerialDevice
from bluetooth.concrete.device import SerialBluetoothDevice
from bluetooth.framework.device import BluetoothDevice
from bluetooth.framework.monitor import SerialListener
from robot.concrete.crt_robot import SerialRobot, BytePrintedRobot
from robot.framework.fw_robot import Robot

bt_dev = ".*CP2102.*"
bot_dev = ".*FT232R.*"

debug = False
debug_robot_port = 'COM1'
debug_bt_port = 'COM3'


def getBluetooth(listener: SerialListener) -> BluetoothDevice:
    if debug:
        bt = SerialBluetoothDevice(debug_bt_port, listener)
        if not bt.isOpen():
            bt.open()
        return bt

    for port in comports():
        if re.search(bt_dev, port.description):
            bt = SerialBluetoothDevice(port.device, listener)
            if not bt.isOpen():
                bt.open()
            return bt
    raise Exception(bt_dev + " not found.")


def getRobot() -> Robot:
    if debug:
        bot = BytePrintedRobot()
        # bot = SerialRobot(LocalSerialDevice(Serial(debug_robot_port, baudrate=57142, timeout=0.5, write_timeout=1)))
        if not bot.isOpen():
            bot.open()
        return bot

    for port in comports():
        if re.search(bot_dev, port.description):
            bot = SerialRobot(LocalSerialDevice(Serial(port.device, baudrate=57142, timeout=0.5, write_timeout=1)))
            if not bot.isOpen():
                bot.open()
            return bot

    raise Exception(bot_dev + " not found.")
