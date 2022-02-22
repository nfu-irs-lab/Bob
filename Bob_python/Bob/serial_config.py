import re

import serial
from serial.tools.list_ports_linux import comports

from Bob.communication.framework.fw_package_device import PackageDevice
from Bob.device.concrete.crt_serial_dev import LocalSerialDevice
from Bob.communication.concrete.crt_package_device import SerialPackageDevice


def getBTSerial(device):
    return LocalSerialDevice(
        serial.Serial(device, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=10000),
        write_delay_ms=0)


def getSerialNameByDescription(description: str):
    for port in comports():
        if re.search(description, port.description):
            return port.device
    raise Exception(description + " not found.")


def getBluetoothPackageDeviceWithDescription(description: str) -> PackageDevice:
    ser = SerialPackageDevice(getBTSerial(getSerialNameByDescription(description)))
    ser.open()
    return ser


def getBluetoothPackageDeviceWithName(name: str) -> PackageDevice:
    ser = SerialPackageDevice(getBTSerial(name))
    ser.open()
    return ser
