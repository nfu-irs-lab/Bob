import os
import threading
import time

import cv2
import keyboard

from Bob.communication.framework.fw_package_device import PackageDevice, PackageListener
from Bob.dbctrl.concrete.crt_database import JSONDatabase
import base64
import json
from typing import List, Optional

from Bob.visual.monitor.concrete.crt_camera import CameraMonitor
from Bob.visual.monitor.framework.fw_monitor import CameraListener
from Bob.visual.utils import visual_utils
from command_utils import getCommandsFromFileName
from device_config import getRobot

obj_db_location = f"db{os.path.sep}objects.json"
face_db_location = f"db{os.path.sep}faces.json"
stories_db_location = f"db{os.path.sep}stories.json"
vocabularies_db_location = f"db{os.path.sep}vocabularies.json"
db_charset = 'UTF-8'

object_db = JSONDatabase(open(obj_db_location, encoding=db_charset))
face_db = JSONDatabase(open(face_db_location, encoding=db_charset))
stories_db = JSONDatabase(open(stories_db_location, encoding=db_charset))
vocabularies_db = JSONDatabase(open(vocabularies_db_location, encoding=db_charset))

robot = getRobot()


class MainCameraListener(CameraListener):
    def __init__(self, device: PackageDevice):
        self.device = device
        self.object_timer = 0
        self.face_timer = 0

    def onImageRead(self, image):
        cv2.imshow("face", image)
        cv2.imshow("object", image)

    def onDetect(self, detector_id, image, data):
        if detector_id == 1:
            labeledImage = image
            for result in data:
                label = result['emotion']
                labeledImage = visual_utils.annotateLabel(labeledImage, (result['x']['min'], result['y']['min']),
                                                          (result['x']['max'], result['y']['max']), label,
                                                          overwrite=False)

            cv2.imshow("face", labeledImage)
            if time.time() <= self.face_timer:
                return
            obj: Optional[json] = face_db.queryForId(data[0]['emotion'])

            if obj is not None:
                data: json = obj['data']
                sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
                jsonString = json.dumps(sendData, ensure_ascii=False)
                print("Send:", jsonString)
                self.device.writeString(jsonString)
                self.face_timer = time.time() + 17

        elif detector_id == 2:
            labeledImage = image
            max_index = -1
            max_conf = -1
            i = 0
            for result in data:
                label = result['name'] + " " + str(round(result['conf'], 2))
                labeledImage = visual_utils.annotateLabel(image, (result['x']['min'], result['y']['min']),
                                                          (result['x']['max'], result['y']['max']), label,
                                                          overwrite=False)
                if result['conf'] > max_conf:
                    max_conf = result['conf']
                    max_index = i

                i = i + 1
            cv2.imshow("object", labeledImage)

            if time.time() <= self.object_timer:
                return

            selected_object = data[max_index]
            obj: Optional[json] = object_db.queryForId(selected_object['name'])
            if obj is not None:
                data: json = obj['data']
                sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
                jsonString = json.dumps(sendData, ensure_ascii=False)
                print("Send:", jsonString)
                self.device.writeString(jsonString)
                self.object_timer = time.time() + 17


robot.open()

robot.enableAllServos(True)


# keyboard_ctl = KeyboardController(robot)
# keyboard_ctl.init()


def formatDataToJsonString(id: int, type: str, content: str, data):
    sendData = {"id": id, "response_type": type, "content": content,
                "data": data}
    return json.dumps(sendData, ensure_ascii=False)


def doAction(action):
    print("do action:", action)
    robot.doCommands(getCommandsFromFileName(action))


class CommandControlListener(PackageListener):
    def __init__(self, device: PackageDevice, camera_monitor: CameraMonitor):
        self.__id_counter = 0
        self._camera_monitor = camera_monitor
        self.package_device = device
        self.mode: str = ""

    def onReceive(self, cmd:str):
        """
        當接收到互動介面所傳輸之指令時會被呼叫
        @param cmd:接收到之指令
        """

        print("receive:", cmd)

        if cmd == "DETECT_OBJECT" or cmd == "DETECT_INTER_OBJECT":
            # 開啟物品辨識Detector,關閉臉部辨識Detector
            self._camera_monitor.setDetectorEnable(1, False)
            self._camera_monitor.setDetectorEnable(2, True)
            self.mode = cmd

        elif cmd == "DETECT_FACE":
            # 開啟臉部辨識Detector,關閉物品辨識Detector
            self._camera_monitor.setDetectorEnable(1, True)
            self._camera_monitor.setDetectorEnable(2, False)
        elif cmd == "START_DETECT":
            pass
        elif cmd == "PAUSE_DETECT":
            pass
        elif cmd == "STOP_DETECT":
            pass
        elif cmd == "DB_GET_ALL":
            # 送出所有物品之資料
            all_data: json = object_db.getAllData()
            jsonString = formatDataToJsonString(0, "json_object", "all_objects", all_data)
            print("Send:", jsonString)
            self.package_device.writeString(jsonString)

        elif cmd.startswith("STORY_GET"):
            l1 = cmd[10:]
            if l1 == "LIST":
                # 送出所有故事標題以及資訊
                print("list all")
                stories_list = []
                all_data: json = stories_db.getAllData()
                for story in all_data:
                    stories_list.append(
                        {"id": story['id'], "name": (story['data']['name']), "total": (story['data']['total'])})

                jsonString = formatDataToJsonString(0, "json_array", "all_stories_info", stories_list)
                print("Send:", jsonString)
                self.package_device.writeString(jsonString)
            elif l1.startswith("STORY"):
                # 送出指定故事之所有內容
                story_id = l1[6:]
                print("get story", story_id)
                story_content = stories_db.queryForId(story_id)
                jsonString = formatDataToJsonString(0, "json_object", "story_content", story_content['data'])
                print("Send:", jsonString)
                self.package_device.writeString(jsonString)
        elif cmd.startswith("DO_ACTION"):
            # 機器人做出動作 DO_ACTION [動作名稱].csv
            action = cmd[10:]
            threading.Thread(target=doAction, args=(action,)).start()
            # doAction(action)
        elif cmd == "STOP_ALL_ACTION":
            # 停止機器人所有動作
            robot.stopAllAction()
        elif cmd == "ALL_VOCABULARIES":
            # 送出所有單字資訊
            print("get all vocabulary")
            vocabularies_content = vocabularies_db.queryForId("vocabulary")
            print(vocabularies_content)
            jsonString = formatDataToJsonString(0, "json_array", "all_vocabularies", vocabularies_content['data'])
            print("Send:", jsonString)
            self.package_device.writeString(jsonString)
