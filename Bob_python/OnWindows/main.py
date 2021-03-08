import threading

import serial
import base64
import io
from time import time


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


g_ser = None


def pipScanner(ser: serial.Serial):
    while True:
        while ser.in_waiting > 0:
            data = ser.readall()
            print(data)
            print("Receive")
            print(data.decode("UTF-8"))


with serial.Serial("COM5", 57600, timeout=1, parity=serial.PARITY_NONE) as ser:
    t = threading.Thread(target=pipScanner, args=(ser,))
    t.start()
    while True:
        data = input().encode("UTF-8")
        line = bytearray(len(data) + 1)
        line[0:len(data)] = data
        line[-1] = 0xa
        ser.write(data)
        pass
