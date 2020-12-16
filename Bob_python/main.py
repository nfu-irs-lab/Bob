import serial
import Protocol.protocol_module as pro

with serial.Serial("COM3", 57600, timeout=1,parity=serial.PARITY_NONE) as ser:
    print(ser.is_open)
    availableBytes=ser.in_waiting
    while True:
        if availableBytes>0:
            bs=ser.read(availableBytes)
            print(pro.PackageType.getPackageType(rawbytes=bs))
        availableBytes = ser.in_waiting