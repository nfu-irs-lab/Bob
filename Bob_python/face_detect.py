import base64
import json
import os
import time
from typing import Optional, List

from serial import SerialTimeoutException

from communication.concrete.crt_monitor import ReadLineStrategy, SerialPackageMonitor
from communication.concrete.crt_package import Base64LinePackage, StringPackage
from communication.framework.fw_monitor import SerialListener
from communication.framework.fw_package import Package
from dbctrl.concrete.crt_database import JSONDatabase
from detector.concrete.face_detect_deepface import FaceDetector
from detector.framework.detector import DetectListener
from robot.framework.fw_action import Action
from serial_utils import getRobot, getBluetoothPackageDevice


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
    if btSerial.isOpen():
        try:
            btSerial.writePackage(package)
        except SerialTimeoutException:
            print("bt serial timeout")
        bt_done = True


class RobotControlListener(SerialListener):
    def onReceive(self, data: bytes):
        global running
        d = base64.decodebytes(data)
        cmd = d.decode()
        print("receive:", cmd)
        if cmd == "START_DETECT":
            detector.resume()
            print("Start detect")
        elif cmd == "PAUSE_DETECT":
            detector.pause()
            print("Pause detect")
        elif cmd == "STOP_DETECT":
            running = False


class FaceDetectListener(DetectListener):
    def onDetect(self, face_type: str):
        print("now face emotion: " + face_type)
        obj: Optional[json] = db.queryForId(face_type)
        if obj is not None:
            data: json = obj['data']
            sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
            jsonString = json.dumps(sendData, ensure_ascii=False)
            print("Send:", jsonString)
            pushDataToBluetooth(Base64LinePackage(StringPackage(jsonString, 'UTF-8')))


db_location = f"db{os.path.sep}faces.json"
db_charset = 'UTF-8'
db = JSONDatabase(open(db_location, encoding=db_charset))
robot = getRobot()
robot_done = True
bt_done = True

detector = FaceDetector(FaceDetectListener())
btSerial = getBluetoothPackageDevice()
monitor = btSerial.getMonitor(RobotControlListener(), ReadLineStrategy())
monitor.start()
detector.start()
running = True
try:
    while running:
        time.sleep(1)
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

detector.stop()
monitor.stop()
btSerial.close()
