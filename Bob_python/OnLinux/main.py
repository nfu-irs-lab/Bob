import serial
import threading
import Protocol.protocol_module as pro
import io
from time import time


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


g_ser = None


class MainListener(pro.ProtocolListener):
    def OnProtocolConnected(self):
        pass

    def OnProtocolDisconnected(self):
        pass

    def OnReceiveDataPackage(self, data: bytes):
        pass

    def OnWrite(self, data: bytes):
        print("[Write]")
        dumpByteInHex(data)
        g_ser.write(data)


def job(ser: serial.Serial):
    while True:
        # print("job")
        buffer = bytearray(1024)
        availableBytes = ser.in_waiting
        if availableBytes <= 0:
            continue

        print("received")
        received_len = ser.readinto(buffer)
        # buffer=ser.read(1024)
        data = buffer[0:received_len]
        dumpByteInHex(data)
        bais = io.BytesIO(data)
        while True:
            headerBytes = bytearray(4)
            len = bais.readinto(headerBytes)
            if len != 4:
                break
            header = pro.PackageHeader(headerBytes=headerBytes)
            dumpByteInHex(headerBytes)
            lackBytes = bais.read(header.lackBytesLength)
            dumpByteInHex(lackBytes)
            socket.received(header_bytes=headerBytes, lack_bytes=lackBytes)

        # ser.flushInput()


with serial.Serial("/dev/ttyUSB0", 38400, timeout=1, parity=serial.PARITY_NONE) as ser:
    print(ser.is_open)
    socket = pro.ServerProtocolSocket()
    socket.attach(MainListener())
    g_ser = ser
    t = threading.Thread(target=job, args=(ser,))
    t.start()
    while True:
        if socket.isConnected():
            input()
            data = "WwogIHsKICAibmFtZSI6ImFwcGxlIiwKICAibnVtYmVyIjozCiAgfSwKICB7CiAgICAibmFtZSI6InBlbiIsCiAgICAibnVtYmVyIjo0CiAgfSx7CiAgICAibmFtZSI6ImZ1Y2siLAogICAgIm51bWJlciI6NQogICAgCiAgfQpd".encode()
            socket.writeBytes(data)
        pass
