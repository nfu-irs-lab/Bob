import RPi.GPIO as GPIO
from time import sleep


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(26, GPIO.OUT)
GPIO.setup(19, GPIO.OUT)
GPIO.setup(13, GPIO.OUT)
GPIO.setup(6, GPIO.OUT)

while True:
	
	GPIO.output(26, 0)
	GPIO.output(19, 0)
	GPIO.output(13, 0)
	GPIO.output(6, 1)
	sleep(2)
	
