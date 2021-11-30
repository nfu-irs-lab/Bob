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
from detector.concrete.face import FaceDetector
from detector.framework.detector import DetectListener, Detector
from robotics.framework.action import Action
from serial_utils import getRobot, getBluetooth


class RobotSerialListener(SerialListener):
    def __init__(self, d: Detector):
        self.detector = d

    def onReceive(self, data: bytes):
        d = base64.decodebytes(data)
        cmd = d.decode()
        if cmd == "START_DETECT":
            self.detector.start()
            print("Start detect")
        elif cmd == "PAUSE_DETECT":
            self.detector.pause()
            print("Pause detect")


db_location = f"db{os.path.sep}faces.json"
db_charset = 'UTF-8'
db = FileDatabase(open(db_location, encoding=db_charset), JSONDataParser())
robot = getRobot()
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


class AListener(DetectListener):
    def onDetect(self, face_type: str):
        print("now face emotion: " + face_type)
        obj: Optional[JSONData] = db.queryForId(face_type)
        if obj is not None:
            data: json = obj.getData()
            jsonString = json.dumps(data, ensure_ascii=False)
            print(jsonString)
            pushDataToBluetooth(Base64LinePackage(StringPackage(jsonString, 'UTF-8')))


detector = FaceDetector(AListener())
bt = getBluetooth(RobotSerialListener(detector))
try:
    detector.detect()
except KeyboardInterrupt as e:
    print("Interrupted!!")
    detector.stop()
    bt.close()
