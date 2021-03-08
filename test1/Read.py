#!/user/bin/env python

import serial 
import time

ser2 = serial.Serial(
		port='/dev/ttyACM0',
		baudrate = 9600,
		parity=serial.PARITY_NONE,
		stopbits=serial.STOPBITS_ONE,
		bytesize=serial.EIGHTBITS,
		timeout=1
)

while 1:
		x=ser2.readline()
		print x
		print(type(x))
		print(len(x))

