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
            b = ser.readline()
            line=b.decode("UTF-8").replace("\n","")

            print("Receive")
            print(b)
            print(line)


with serial.Serial("COM4", 38400, timeout=1, parity=serial.PARITY_NONE) as ser:
    t = threading.Thread(target=pipScanner, args=(ser,))
    t.start()
    while True:
        msg = input("Read:")
        ser.write((msg + "\n").encode("UTF-8"))
        pass
