import serial
from time import time

def dumpByteInHex(raw):
    print("[", raw.hex(), "]")

with serial.Serial("/dev/ttyUSB0", 38400, timeout=1,parity=serial.PARITY_NONE) as ser:
    print(ser.is_open)
    availableBytes=ser.in_waiting
    while True:
        if availableBytes>0:
            bs=ser.read(availableBytes)
            print("raw:")
            dumpByteInHex(bs)
            print("str:")
            print(bs.decode("UTF-8"))
        availableBytes = ser.in_waiting
