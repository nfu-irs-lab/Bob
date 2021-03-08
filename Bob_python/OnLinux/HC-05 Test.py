import threading

import serial
import base64
import io
from time import time


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


g_ser = None


def serial_monitor(ser: serial.Serial):
    while True:
        while ser.in_waiting > 0:
            b = ser.readline()
            line = b.decode("UTF-8").replace("\n", "")

            print("Receive")
            print(b)
            print(line)


def writeLine(ser: serial, msg: str):
    ser.write((msg + "\n").encode("UTF-8"))


def writeBase64Line(ser: serial, msg: str):
    encoded_str = base64.b64encode(msg.encode("UTF-8"))
    ser.write(encoded_str)
    ser.write("\n".encode("UTF-8"))


with serial.Serial("/dev/ttyUSB0", 38400, timeout=1, parity=serial.PARITY_NONE) as ser:
    t = threading.Thread(target=serial_monitor, args=(ser,))
    t.start()
    while True:
        msg = input("Read:")
        # writeLine(ser, msg)
        writeBase64Line(ser,msg)
        pass
