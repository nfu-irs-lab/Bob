import base64
import json
import os
import platform
import re
import time
from typing import Optional

import cv2
from deepface import DeepFace
from serial import SerialException, SerialTimeoutException

from bluetooth.concrete.device import SerialBluetoothDevice
from bluetooth.concrete.package import Base64LinePackage, StringPackage
from bluetooth.framework.monitor import SerialListener
from bluetooth.framework.package import Package
from dbctrl.concrete import queryJsonFromName
from dbctrl.concrete.database import FileDatabase
from dbctrl.concrete.face import JSONFaceParser, Face
from dbctrl.concrete.object import JSONObjectParser
from robotics.concrete.command import RoboticsCommandFactory
from robotics.concrete.robot import RoboticsRobot
from robotics.framework.action import CSVAction, Action
from serial.tools.list_ports_linux import comports
from serial_utils import getRobot, getBluetooth


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


db_location = f"db{os.path.sep}faces.json"
db_charset = 'UTF-8'
db = FileDatabase(open(db_location, encoding=db_charset), JSONFaceParser())
robot = getRobot()
bt = getBluetooth(RobotSerialListener())
detect = True
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
cap = cv2.VideoCapture(0)

while True:
    while not detect:
        time.sleep(1)

    ret, frame = cap.read()
    cv2.imshow('raw', frame)

    try:
        result = DeepFace.analyze(frame, actions=['emotion'])
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        faces = faceCascade.detectMultiScale(gray, 1.1, 4)

        for (x, y, w, h) in faces:
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

        font = cv2.FONT_HERSHEY_SIMPLEX

        cv2.putText(frame, result['dominant_emotion'], (50, 50), font, 3, (0, 0, 255), 2, cv2.LINE_4)

        cv2.imshow('result', frame)
        face_type = result['dominant_emotion']
        print("now face emotion: " + face_type)
        face: Optional[Face] = db.queryForId(face_type)
        if face is not None:
            js = queryJsonFromName(face.name, open(db_location, encoding=db_charset))
            jsonString = json.dumps(js, ensure_ascii=False)
            print(jsonString)
            pushDataToBluetooth(Base64LinePackage(StringPackage(jsonString, 'UTF-8')))
    except Exception as e:
        print(e.__str__())
        pass
    if cv2.waitKey(2) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyWindow()
