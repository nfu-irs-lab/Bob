
#!/user/bin/env python

import serial 
import time
import sys

import RPi.GPIO as GPIO
from time import sleep


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(26, GPIO.OUT)
GPIO.setup(19, GPIO.OUT)
GPIO.setup(13, GPIO.OUT)
GPIO.setup(6, GPIO.OUT)

def Robotis(id, pos, speed):
    ser = serial.Serial('/dev/ttyUSB3',  57142, timeout= 0.5);
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
		GPIO.output(26, 0)
		GPIO.output(19, 1)
		GPIO.output(13, 1)
		GPIO.output(6, 0)
		Robotis(1,900,200)
		Robotis(2,1840,50)
		Robotis(3,2730,50)
		Robotis(4,30,50)
		Robotis(6,1060,200)
		Robotis(7,28,50)
		Robotis(8,480,50)
		Robotis(9,336,50)		
		time.sleep(2)
		GPIO.output(26, 0)
		GPIO.output(19, 1)
		GPIO.output(13, 0)
		GPIO.output(6, 1) 
		Robotis(1,2800,200)
		Robotis(2,2300,50)
		Robotis(3,2151,50)
		Robotis(4,30,50)
		Robotis(6,3000,200)
		Robotis(7,359,50)
		Robotis(8,480,50)
		Robotis(9,336,50)		           
		time.sleep(2)

		
        

        

	
