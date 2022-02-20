from Bob.device.framework.fw_device import SerialDevice, Device
import os

from Bob.robot.framework.fw_servo import DynamixelServo

if os.name == 'nt':
    import msvcrt


    def getch():
        return msvcrt.getch().decode()
else:
    import sys, tty, termios

    fd = sys.stdin.fileno()
    old_settings = termios.tcgetattr(fd)


    def getch():
        try:
            tty.setraw(sys.stdin.fileno())
            ch = sys.stdin.read(1)
        finally:
            termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
        return ch

from dynamixel_sdk import *  # Uses Dynamixel SDK library

# Default setting
BAUDRATE = 57600  # Dynamixel default baudrate : 57600

# Protocol version
PROTOCOL_VERSION1 = 1.0  # See which protocol version is used in the Dynamixel
PROTOCOL_VERSION2 = 2.0

# Initialize PacketHandler instance
# Set the protocol version
# Get methods and members of Protocol1PacketHandler or Protocol2PacketHandler
packetHandler1 = PacketHandler(PROTOCOL_VERSION1)
packetHandler2 = PacketHandler(PROTOCOL_VERSION2)


class DynamixelRobot(Device):

    def __init__(self, device_name: str, baudrate: int):
        self._baudrate = baudrate
        self._portHandler = PortHandler(device_name)

    def open(self):
        if self._portHandler.openPort():
            print("Succeeded to open the port")
        else:
            print("Failed to open the port")
            exit(1)

        # Set port baudrate
        if self._portHandler.setBaudRate(self._baudrate):
            print("Succeeded to change the baudrate")
        else:
            print("Failed to change the baudrate")
            exit(2)

    def close(self):
        self._portHandler.closePort()

    def isOpen(self) -> bool:
        return self._portHandler.is_open

    # def write(self, protocol: int, id: int, address: int, value, byte_num: int):
    #     if protocol == 1:
    #         self._write_proto_1(id, address, value, byte_num)
    #     elif protocol == 2:
    #         self._write_proto_2(id, address, value, byte_num)

    def _write_proto_1(self, id: int, address: int, value, byte_num: int):
        if byte_num == 4:
            dxl_comm_result, dxl_error = packetHandler1.write4ByteTxRx(self._portHandler, id, address, value)
        elif byte_num == 1:
            dxl_comm_result, dxl_error = packetHandler1.write1ByteTxRx(self._portHandler, id, address, value)
        else:
            raise Exception("byte_num=" + byte_num)

        if dxl_comm_result != COMM_SUCCESS:
            raise Exception("%s" % packetHandler1.getTxRxResult(dxl_comm_result))
        elif dxl_error != 0:
            raise Exception("%s" % packetHandler1.getRxPacketError(dxl_error))

    def _write_proto_2(self, id: int, address: int, value, byte_num: int):
        if byte_num == 4:
            dxl_comm_result, dxl_error = packetHandler2.write4ByteTxRx(self._portHandler, id, address, value)
        elif byte_num == 1:
            dxl_comm_result, dxl_error = packetHandler2.write1ByteTxRx(self._portHandler, id, address, value)
        else:
            raise Exception("byte_num=" + byte_num)

        if dxl_comm_result != COMM_SUCCESS:
            print("%s" % packetHandler2.getTxRxResult(dxl_comm_result))
        elif dxl_error != 0:
            print(dxl_error)
            raise Exception("%s" % packetHandler2.getRxPacketError(dxl_error))

    def _read_proto_1(self, id: int, address: int, byte_num: int):
        if byte_num == 4:
            value, dxl_comm_result, dxl_error = packetHandler1.packetHandler1.read4ByteTxRx(self._portHandler, id,
                                                                                            address)
        elif byte_num == 1:
            value, dxl_comm_result, dxl_error = packetHandler1.packetHandler1.read1ByteTxRx(self._portHandler, id,
                                                                                            address)
        else:
            raise Exception("byte_num=" + byte_num)

        if dxl_comm_result != COMM_SUCCESS:
            print("%s" % packetHandler1.getTxRxResult(dxl_comm_result))
        elif dxl_error != 0:
            print("%s" % packetHandler1.getRxPacketError(dxl_error))

        return value

    def _read_proto_2(self, id: int, address: int, byte_num: int):
        if byte_num == 4:
            value, dxl_comm_result, dxl_error = packetHandler2.packetHandler1.read4ByteTxRx(self._portHandler, id,
                                                                                            address)
        elif byte_num == 1:
            value, dxl_comm_result, dxl_error = packetHandler2.packetHandler1.read1ByteTxRx(self._portHandler, id,
                                                                                            address)
        else:
            raise Exception("byte_num=" + byte_num)

        if dxl_comm_result != COMM_SUCCESS:
            print("%s" % packetHandler1.getTxRxResult(dxl_comm_result))
        elif dxl_error != 0:
            print("%s" % packetHandler1.getRxPacketError(dxl_error))
        return value

    def enableToque(self, servo: DynamixelServo, enable: bool):
        servoId = servo.getId()
        if enable:
            value = 1
        else:
            value = 0

        if servo.getProtocol() == 1:
            self._write_proto_1(servoId, servo.getTorqueEnableAddress(), value, 1)

        if servo.getProtocol() == 2:
            self._write_proto_2(servoId, servo.getTorqueEnableAddress(), value, 1)

    def setGoalPosition(self, servo: DynamixelServo, position: int):
        servoId = servo.getId()
        if servo.getProtocol() == 1:
            self._write_proto_1(servoId, servo.getGoalPositionAddress(), position, 4)
        if servo.getProtocol() == 2:
            self._write_proto_2(servoId, servo.getGoalPositionAddress(), position, 4)

    def setVelocity(self, servo: DynamixelServo, velocity: int):
        servoId = servo.getId()
        if servo.getProtocol() == 1:
            self._write_proto_1(servoId, servo.getGoalPositionAddress(), velocity, 4)
        if servo.getProtocol() == 2:
            self._write_proto_2(servoId, servo.getGoalPositionAddress(), velocity, 4)
