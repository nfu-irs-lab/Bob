import base64
import platform
import re
import time

import cv2
from deepface import DeepFace
from serial import SerialException, SerialTimeoutException

from bluetooth.concrete.device import SerialBluetoothDevice
from bluetooth.concrete.package import Base64LinePackage, StringPackage
from bluetooth.framework.monitor import SerialListener
from bluetooth.framework.package import Package
from dbctrl.concrete.database import JSONDatabase
from robotics.concrete.command import RoboticsCommandFactory
from robotics.concrete.robot import RoboticsRobot
from robotics.framework.action import CSVAction, Action
from serial.tools.list_ports_linux import comports

detect = False


class RobotSerialListener(SerialListener):

    def onReceive(self, data: bytes):
        global detect
        d = base64.decodebytes(data)
        cmd = d.decode()
        if cmd == "START_DETECT":
            print("Start detect")
            detect = True
        elif cmd == "PAUSE_DETECT":
            print("Pause detect")
            detect = False


def getBluetoothCom(default_bt_com: str):
    comList = comports()
    for port in comList:
        if re.search(".*CP2102.*", port.description):
            return port.device

    return default_bt_com


def getRobotCom(default_robot_com: str):
    comList = comports()
    for port in comList:
        if re.search(".*FT232R.*", port.description):
            return port.device

    return default_robot_com


def getActionFromName(name: str, file_separator: str) -> Action:
    return CSVAction(f'actions{file_separator}{name}.csv', RoboticsCommandFactory())


print("System:", platform.system())
bt_default = ""
robot_default = ""
separator = ""
if platform.system() == "Windows":
    separator = "\\"
    bt_default = "COM3"
    robot_default = "COM1"
elif platform.system() == "Linux":
    separator = "/"
    bt_default = "/dev/ttyUSB0"
    robot_default = "/dev/ttyUSB1"

db_location = f"db{separator}objects.json"
db_charset = 'UTF-8'

db = JSONDatabase(open(db_location, encoding=db_charset))

try:
    bt = SerialBluetoothDevice(getBluetoothCom(bt_default), RobotSerialListener())
    if not bt.isOpen():
        bt.open()
except SerialException as e:
    print(e)
    exit(1)

try:
    robot = RoboticsRobot(getRobotCom(robot_default))
    if not robot.isOpen():
        robot.open()

    robot.doAction(getActionFromName("reset", separator))
except SerialException as e:
    print(e)
    exit(2)

robot_done = True
bt_done = True


def pushActionToRobot(action: Action):
    global robot_done
    if robot.isOpen():
        try:
            robot.doAction(action)
        except SerialTimeoutException:
            print("robot serial timeout")
        robot_done = True


def pushDataToBluetooth(package: Package):
    global bt_done
    if bt.isOpen():
        try:
            bt.write(package)
        except SerialTimeoutException:
            print("bt serial timeout")
        bt_done = True


faceCascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)

while True:
    while not detect:
        time.sleep(1)
    ret, frame = cap.read()
    try:
        result = DeepFace.analyze(frame, actions=['emotion'])
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        faces = faceCascade.detectMultiScale(gray, 1.1, 4)

        for (x, y, w, h) in faces:
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

        font = cv2.FONT_HERSHEY_SIMPLEX

        cv2.putText(frame, result['dominant_emotion'], (50, 50), font, 3, (0, 0, 255), 2, cv2.LINE_4)
        print("now face emotion: " + result['dominant_emotion'])
        pushDataToBluetooth(Base64LinePackage(StringPackage(result['dominant_emotion'], 'UTF-8')))
    except Exception as e:
        pass
    cv2.imshow('title', frame)

    if cv2.waitKey(2) & 0xFF == ord('q'):
        break
cap.release()
cv2.destroyWindow()
