import socket
from typing import Optional, List

import serial

from communication.framework.fw_comm import CommDevice, PackageHandler


class SerialCommDevice(CommDevice):

    def __init__(self, port_name: str, baudrate: int, handler: PackageHandler):
        self.serial = serial.Serial(port_name, baudrate)
        self.handler = handler

    def read(self) -> Optional[bytes]:
        self.handler.handle(self.serial.read())
        if not self.handler.hasPackage():
            return None
        else:
            return self.handler.getPackageAndNext()

    def write(self, data: bytes) -> int:
        return self.serial.write(self.handler.convertToPackage(data))

    def open(self):
        if self.isOpen():
            self.serial.open()

    def close(self):
        self.serial.close()

    def isOpen(self) -> bool:
        return self.serial.isOpen()


class TCPCommDevice(CommDevice):

    def __init__(self, socket: socket.socket, handler: PackageHandler):
        self.socket = socket
        self.handler = handler

    def read(self) -> Optional[bytes]:
        self.handler.handle(self.socket.recv(4096))
        if not self.handler.hasPackage():
            return None
        else:
            return self.handler.getPackageAndNext()

    def write(self, data: bytes) -> int:
        return self.socket.send(self.handler.convertToPackage(data))

    def open(self):
        pass

    def close(self):
        self.socket.close()


# 預設的結尾符號
EOP = bytes([0xe2, 0x80, 0xA9])


# 傳輸資料時所需要之通訊協定
class EOLPackageHandler(PackageHandler):
    def __init__(self, EndOfLine: bytes = EOP):
        self.__EOL = EndOfLine
        self.buffer = bytearray()
        self.delay_timer = 0
        self.packages: List[bytes] = []

    # 將接收到的原始位元組陣列進行解析
    def handle(self, data: bytes):
        for b in data:
            self.buffer.append(b)
        indexOfFirstEOL = self.__getIndexOfFirstEOL(self.buffer)
        while indexOfFirstEOL != -1:
            self.packages.append(self.buffer[0:indexOfFirstEOL])
            del self.buffer[0:indexOfFirstEOL + len(self.__EOL)]
            indexOfFirstEOL = self.__getIndexOfFirstEOL(self.buffer)

    # 當有封包尚未被讀取時會回傳True,反之為False
    def hasPackage(self) -> bool:
        return len(self.packages) > 0

    # 取得封包,並跳至下一個封包
    def getPackageAndNext(self) -> bytes:
        if self.hasPackage():
            b = self.packages[0]
            del self.packages[0]
            return b
        else:
            raise RuntimeError("No package")

    # 將原始資料編碼加上EOL並回傳位元組陣列
    def convertToPackage(self, data: bytes) -> bytes:
        buffer = bytearray(len(data) + len(self.__EOL))
        buffer[0:len(data)] = data
        buffer[len(data):] = self.__EOL
        return bytes(buffer)

    # 取得EOL位於字串之位置
    def __getIndexOfFirstEOL(self, data):
        for i in range(0, len(data)):
            found = 0
            for j in range(0, len(self.__EOL)):
                if i + j < len(data):
                    a = self.__EOL[j]
                    b = data[i + j]
                    if b == a:
                        found += 1
            if found == len(self.__EOL):
                return i
        return -1
