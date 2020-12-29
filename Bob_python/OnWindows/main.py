import serial
import Protocol.protocol_module as pro
from time import time

connected=False

def OnClientHelloReceive(data_bytes,ser):
    clientHello = pro.ClientHelloPackage(rawbytes=data_bytes)
    if clientHello.verify():

        global connected
        serverHello = pro.ServerHelloPackage(statusCode=pro.StatusCode.ALLOW.value)
        connected=True
    else:
        serverHello = pro.ServerHelloPackage(statusCode=pro.StatusCode.NOT_SUPPORT.value)
        connected=False

    ser.write(serverHello.toBytes())

def OnReceivePackage(data_bytes,ser):
    type=pro.PackageType.getPackageType(rawbytes=data_bytes)
    print(type)
    if type==pro.PackageType.ClientHello:
        OnClientHelloReceive(data_bytes,ser)



with serial.Serial("COM3", 57600, timeout=1,parity=serial.PARITY_NONE) as ser:
    print(ser.is_open)
    availableBytes=ser.in_waiting
    timer=int(time() * 1000)
    while True:
        if connected and int(time() * 1000)>=timer:
            timer=int(time() * 1000)+5000
            str="WwogIHsKICAibmFtZSI6ImFwcGxlIiwKICAibnVtYmVyIjozCiAgfSwKICB7CiAgICAibmFtZSI6InBlbiIsCiAgICAibnVtYmVyIjo0CiAgfSx7CiAgICAibmFtZSI6ImZ1Y2siLAogICAgIm51bWJlciI6NQogICAgCiAgfQpd"
            datapackages=pro.DataPackage.splitPackage(str.encode("UTF-8"))
            for datapackage in datapackages:
                ser.write(datapackage.toBytes())


        if availableBytes>0:
            bs=ser.read(availableBytes)
            OnReceivePackage(bs,ser)

        availableBytes = ser.in_waiting