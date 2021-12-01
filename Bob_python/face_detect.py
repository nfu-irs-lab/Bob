import base64
import json
import os
from typing import Optional, List

from serial import SerialTimeoutException

from bluetooth.concrete.package import Base64LinePackage, StringPackage
from bluetooth.framework.monitor import SerialListener
from bluetooth.framework.package import Package
from dbctrl.concrete.database import FileDatabase
from dbctrl.concrete.json_data import JSONData, JSONDataParser
from detector.concrete.face_detect_deepface import FaceDetector
from detector.framework.detector import DetectListener
from robot.framework.fw_action import Action
from serial_utils import getRobot, getBluetooth


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


class RobotControlListener(SerialListener):
    def onReceive(self, data: bytes):
        d = base64.decodebytes(data)
        cmd = d.decode()
        if cmd == "START_DETECT":
            detector.start()
            print("Start detect")
        elif cmd == "PAUSE_DETECT":
            detector.pause()
            print("Pause detect")


class FaceDetectListener(DetectListener):
    def onDetect(self, face_type: str):
        print("now face emotion: " + face_type)
        obj: Optional[JSONData] = db.queryForId(face_type)
        if obj is not None:
            data: json = obj.getData()
            jsonString = json.dumps(data, ensure_ascii=False)
            print(jsonString)
            pushDataToBluetooth(Base64LinePackage(StringPackage(jsonString, 'UTF-8')))


db_location = f"db{os.path.sep}faces.json"
db_charset = 'UTF-8'
db = FileDatabase(open(db_location, encoding=db_charset), JSONDataParser())
robot = getRobot()
robot_done = True
bt_done = True
detector = FaceDetector(FaceDetectListener())
bt = getBluetooth(RobotControlListener())
try:
    detector.detect()
except KeyboardInterrupt as e:
    print("Interrupted!!")
    detector.stop()
    bt.close()
