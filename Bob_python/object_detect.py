"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""

import base64
import json
import os
import threading
from typing import List, Optional
from serial import SerialTimeoutException

from communication.concrete.crt_monitor import SerialPackageMonitor, ReadLineStrategy, PrintedSerialListener
from communication.concrete.crt_package import StringPackage, Base64LinePackage
from communication.framework.fw_monitor import SerialListener
from communication.framework.fw_package import Package
from dbctrl.concrete.crt_database import JSONDatabase
from detector.concrete.object_detect_yolov5 import ObjectDetector
from detector.framework.detector import DetectListener
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


class RobotControlListener(SerialListener):
    def onReceive(self, data: bytes):
        d = base64.decodebytes(data)
        cmd = d.decode()
        print("receive:", cmd)
        if cmd == "START_DETECT":
            detector.resume()
            print("Start detect")
        elif cmd == "PAUSE_DETECT":
            detector.pause()
            print("Pause detect")


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

            obj: Optional[json] = db.queryForId(dobj['name'])
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
        pass


db_location = f"db{os.path.sep}objects.json"
db_charset = 'UTF-8'
db = JSONDatabase(open(db_location, encoding=db_charset))
id_counter = 0
robot_done = True
bt_done = True
robot = getRobot()
detector = ObjectDetector(ObjectDetectListener())
btSerial = getBluetoothPackageDevice()
monitor = btSerial.getMonitor(RobotControlListener(), ReadLineStrategy())
monitor.start()

try:
    detector._detect(source='0', weights='yolov5s.pt')
except KeyboardInterrupt as e:
    print("Interrupted!!")
    detector.stop()
    monitor.stop()
    btSerial.close()
