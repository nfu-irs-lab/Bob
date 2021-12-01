import re

import serial
from serial import Serial
from serial.tools.list_ports_linux import comports

from Bob.device.concrete.crt_serial_dev import LocalSerialDevice
from communication.concrete.crt_package_device import SerialPackageDevice
from communication.framework.fw_monitor import SerialListener
from communication.framework.fw_package_device import PackageDevice
from robot.concrete.crt_robot import SerialRobot, BytePrintedRobot
from robot.framework.fw_robot import Robot

bt_dev = ".*CP2102.*"
bot_dev = ".*FT232R.*"

debug = False
debug_robot_port = 'COM1'
debug_bt_port = 'COM3'


def getBluetoothPackageDevice() -> PackageDevice:
    if debug:
        ser = SerialPackageDevice(LocalSerialDevice(
            serial.Serial(debug_bt_port, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=1)))
        ser.open()
        return ser

    for port in comports():
        if re.search(bt_dev, port.description):
            ser = SerialPackageDevice(LocalSerialDevice(
                serial.Serial(port.device, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=1)))
            ser.open()
            return ser

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
