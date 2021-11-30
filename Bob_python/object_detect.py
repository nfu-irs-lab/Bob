"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""

import base64
import json
import os
import sys
import threading
from pathlib import Path
from typing import List, Optional
from serial import SerialTimeoutException
from bluetooth.concrete.package import StringPackage, Base64LinePackage
from bluetooth.framework.monitor import SerialListener
from bluetooth.framework.package import Package
from dbctrl.concrete.database import FileDatabase
from dbctrl.concrete.json_data import JSONDataParser, JSONData
from detector.concrete.object import ObjectDetector
from detector.framework.detector import DetectListener, Detector
from robotics.concrete.command import RoboticsCommandFactory
from robotics.framework.action import Action, CSVAction

FILE = Path(__file__).absolute()
sys.path.append(FILE.parents[0].as_posix())  # add yolov5/ to path
from serial_utils import getRobot, getBluetooth

db_location = f"db{os.path.sep}objects.json"
db_charset = 'UTF-8'
db = FileDatabase(open(db_location, encoding=db_charset), JSONDataParser())
robot_done = True
bt_done = True
robot = getRobot()


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
    if bt.isOpen():
        try:
            bt.write(package)
        except SerialTimeoutException:
            print("bt serial timeout")
        bt_done = True


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


class AListener(DetectListener):
    def onDetect(self, objectList: List):
        global robot_done
        global bt_done

        if not robot_done or not bt_done:
            return

        for dobj in objectList:
            if dobj['confidence'] < 0.65:
                continue

            obj: Optional[JSONData] = db.queryForId(dobj['name'])
            if obj is not None:
                data: json = obj.getData()
                jsonString = json.dumps(data, ensure_ascii=False)
                print("Send:", jsonString)
                robot_done = False
                bt_done = True
                bt_thread = threading.Thread(target=pushDataToBluetooth,
                                             args=(Base64LinePackage(StringPackage(jsonString, "UTF-8")),))
                bt_thread.start()
                robot_thread = threading.Thread(target=pushActionToRobot, args=(getActionFromFileName(data['action']),))
                robot_thread.start()
                break
        pass


detector = ObjectDetector(AListener())
bt = getBluetooth(RobotSerialListener(detector))
# detector.start()
try:
    detector.detect(source='0', weights='yolov5s.pt')
except KeyboardInterrupt as e:
    print("Interrupted!!")
    detector.stop()
    bt.close()
