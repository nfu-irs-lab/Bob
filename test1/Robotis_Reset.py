#!/user/bin/env python

import serial 
import time

def Robotis(id, pos, speed):
    ser = serial.Serial('/dev/ttyUSB0',  57142, timeout= 0.5);
    ser.bytesize = serial.EIGHTBITS
    arr =[ ]
    arr.append(0xff)
    arr.append(0xff)
    arr.append(id)
    arr.append(0x07)
    arr.append(0x03)
    arr.append(0x1e)
    arr.append(pos & 255)
    arr.append(pos // 256)
    arr.append(speed & 255)
    arr.append(speed // 256)
    tt = 0xff - (sum(arr[2:9]) & 255 )
    arr.append(tt)
    ary = bytearray(arr)
    ser.write(ary)
while 1:
		Robotis(1,2000,50)
		Robotis(6,2030,50)
		Robotis(2,2048,50)
		Robotis(7,2048,50)
		Robotis(3,834,50)
		Robotis(8,200,50)
		Robotis(4,512,50)
		Robotis(9,450,50)             
		time.sleep(3)
        

        

	
