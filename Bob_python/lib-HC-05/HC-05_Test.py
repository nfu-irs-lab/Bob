import time

from models.hc05 import HC05Serial

hc05=HC05Serial('COM4')
while hc05.isOpen:
    print("fuck")
    hc05.writeLine("Fuck")
    time.sleep(1)
