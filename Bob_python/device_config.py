import re

from serial.tools.list_ports_linux import comports

from Bob.communication.concrete.crt_package_device import SerialPackageDevice
from Bob.communication.concrete.crt_strategy import ReadLineStrategy
from Bob.communication.framework.fw_package_device import PackageDevice
from Bob.device.concrete.crt_serial_dev import BluetoothSocketSerialDevice, LocalSerialDevice
from Bob.robot.concrete.crt_dynamixel import Dynamixel, serial
from Bob.robot.concrete.servo_utils import CSVServoAgent


def getSerialNameByDescription(description: str):
    for port in comports():
        if re.search(description, port.description):
            return port.device
    raise Exception(description + " not found.")


# 藍芽HC-05模組 UART/USB轉接器晶片名稱(使用正規表達式)
bt_description = ".*CP2102.*"

# 機器人 UART/USB轉接器晶片名稱(使用正規表達式)
bot_description = ".*FT232R.*"


def getDynamixel() -> Dynamixel:
    """
    取得實體機器人裝置
    @return: 實體機器人
    """
    agent = CSVServoAgent("servos.csv")
    dynamixel = Dynamixel(getSerialNameByDescription(bot_description), 115200)
    for servo in agent.getDefinedServos():
        dynamixel.appendServo(servo)
    return dynamixel


def getBluetooth() -> PackageDevice:
    return getSerialBluetooth()


def getSerialBluetooth() -> PackageDevice:
    return SerialPackageDevice(getBTSerial(getSerialNameByDescription(bt_description)), ReadLineStrategy())


def getSocketBluetooth(socket) -> PackageDevice:
    return SerialPackageDevice(BluetoothSocketSerialDevice(socket, write_delay_ms=100), ReadLineStrategy())


def getBTSerial(device):
    return LocalSerialDevice(
        serial.Serial(device, baudrate=38400, parity=serial.PARITY_NONE, timeout=0.5, write_timeout=10000),
        write_delay_ms=0)
