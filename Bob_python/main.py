"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""

import base64
import json
import os
import threading
import time
from typing import List, Optional
from serial import SerialTimeoutException

from communication.concrete.crt_monitor import SerialPackageMonitor
from communication.concrete.crt_listener import PrintedPackageListener
from communication.concrete.crt_strategy import ReadLineStrategy
from communication.concrete.crt_package import StringPackage, Base64LinePackage
from communication.framework.fw_listener import PackageListener
from communication.framework.fw_package import Package
from dbctrl.concrete.crt_database import JSONDatabase
from detector.concrete.face_detect_deepface import FaceDetector
from detector.concrete.object_detect_yolov5 import ObjectDetector
from detector.framework.detector import Detector, DetectListener
from robot.concrete.crt_command import RoboticsCommandFactory
from robot.framework.fw_action import Action
from robot.concrete.crt_action import CSVAction
from serial_utils import getRobot, getBluetoothPackageDevice


def getActionFromFileName(file: str) -> Action:
    return CSVAction(f'actions{os.path.sep}{file}', RoboticsCommandFactory())


def pushActionToRobot(action: Action):
    global robot_done
    if robot.isOpen():
        try:
            robot.doAction(action)
            robot.doAction(getActionFromFileName("reset.csv"))
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


class CommandControlListener(PackageListener):
    def __init__(self):
        self.__id_counter = 0

    def onReceive(self, data: bytes):
        global detector
        d = base64.decodebytes(data)
        cmd = d.decode()
        print("receive:", cmd)

        if cmd == "DETECT_OBJECT":
            if detector is not None:
                detector.stop()

            detector = ObjectDetector(ObjectDetectListener())
            detector.start()
        elif cmd == "DETECT_FACE":
            if detector is not None:
                detector.stop()

            detector = FaceDetector(FaceDetectListener())
            detector.start()

        elif cmd == "START_DETECT":
            print("Start detect")
            if detector is None:
                return
            detector.resume()
        elif cmd == "PAUSE_DETECT":
            if detector is None:
                return
            print("Pause detect")
            detector.pause()
        elif cmd == "STOP_DETECT":
            if detector is not None:
                detector.stop()

        elif cmd == "DB_GET_ALL":
            all_data: json = obj_db.getAllData()
            sendData = {"id": self.__id_counter, "response_type": "json_object", "content": "all_object",
                        "data": all_data}
            jsonString = json.dumps(sendData, ensure_ascii=False)
            print("Send:", jsonString)
            bt_thread = threading.Thread(target=pushDataToBluetooth,
                                         args=(Base64LinePackage(StringPackage(jsonString, "UTF-8")),))
            bt_thread.start()
            self.__id_counter = self.__id_counter + 1


class FaceDetectListener(DetectListener):
    def onDetect(self, face_type: str):
        print("now face emotion: " + face_type)
        obj: Optional[json] = face_db.queryForId(face_type)
        if obj is not None:
            data: json = obj['data']
            sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
            jsonString = json.dumps(sendData, ensure_ascii=False)
            print("Send:", jsonString)
            pushDataToBluetooth(Base64LinePackage(StringPackage(jsonString, 'UTF-8')))


class ObjectDetectListener(DetectListener):
    def onDetect(self, objectList: List):
        global robot_done
        global bt_done
        global id_counter

        if not robot_done or not bt_done:
            return

        for dobj in objectList:
            if dobj['confidence'] < 0.65:
                continue

            obj: Optional[json] = obj_db.queryForId(dobj['name'])
            if obj is not None:
                data: json = obj['data']
                sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
                jsonString = json.dumps(sendData, ensure_ascii=False)
                print("Send:", jsonString)

                # id_counter = id_counter + 1
                robot_done = False
                bt_done = True
                bt_thread = threading.Thread(target=pushDataToBluetooth,
                                             args=(Base64LinePackage(StringPackage(jsonString, "UTF-8")),))
                bt_thread.start()
                robot_thread = threading.Thread(target=pushActionToRobot, args=(getActionFromFileName(data['action']),))
                robot_thread.start()
                break


obj_db_location = f"db{os.path.sep}objects.json"
face_db_location = f"db{os.path.sep}faces.json"
db_charset = 'UTF-8'
obj_db = JSONDatabase(open(obj_db_location, encoding=db_charset))
face_db = JSONDatabase(open(face_db_location, encoding=db_charset))
id_counter = 0
robot_done = True
bt_done = True
robot = getRobot()
detector = None
btSerial = getBluetoothPackageDevice()
monitor = btSerial.getMonitor(CommandControlListener(), ReadLineStrategy())
monitor.start()

print("Monitor start")
try:
    while True:
        time.sleep(1)
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

if detector is not None:
    detector.stop()
monitor.stop()
btSerial.close()

# try:
#     print("Monitor start")
#     monitor.start()
# except KeyboardInterrupt:
#     print("Main Interrupted")
#     monitor.stop()
#     btSerial.close()
