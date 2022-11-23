import re

from serial.tools.list_ports_linux import comports

from Bob.robot.concrete.crt_dynamixel import Dynamixel, serial
from Bob.robot.concrete.servo_utils import CSVServoAgent
from Bob.communication.concrete.crt_comm import SerialCommDevice, EOLPackageHandler
from Bob.communication.framework.fw_comm import CommDevice


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


def getSerialBluetooth() -> CommDevice:
    return SerialCommDevice(getSerialNameByDescription(bt_description), 38400, EOLPackageHandler())


# def getSocketBluetooth(socket) -> PackageDevice:
#     return SerialPackageDevice(BluetoothSocketSerialDevice(socket, write_delay_ms=100), ReadLineStrategy())
