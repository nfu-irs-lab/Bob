"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""
import csv
import json
import os
import socket
import time
from typing import Optional

import cv2

from Bob.dbctrl.concrete.crt_database import JSONDatabase
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.monitor.concrete.crt_camera import CameraMonitor
from Bob.visual.monitor.framework.fw_monitor import CameraListener
from Bob.visual.utils import visual_utils
from Bob.communication.concrete.crt_comm import TCPCommDevice, EOLPackageHandler, SerialCommDevice
from Bob.communication.framework.fw_comm import CommDevice
from device_config import getSerialBluetooth, getDynamixel

db_charset = 'UTF-8'

# 初始化教學資料庫,載入所有資料
object_db = JSONDatabase(open(f"db{os.path.sep}objects.json", encoding=db_charset))
face_db = JSONDatabase(open(f"db{os.path.sep}faces.json", encoding=db_charset))
stories_db = JSONDatabase(open(f"db{os.path.sep}stories.json", encoding=db_charset))
vocabularies_db = JSONDatabase(open(f"db{os.path.sep}vocabularies.json", encoding=db_charset))


class MainCameraListener(CameraListener):
    def __init__(self, commDevice: CommDevice):
        self.commDevice = commDevice
        self.object_timer = 0
        self.face_timer = 0

    # 當從攝影機擷取到照片時,此方法被觸發
    def onImageRead(self, image):
        # 顯示預覽視窗
        cv2.imshow("face", image)
        cv2.imshow("object", image)

    def onDetect(self, detector_id, image, data):
        """
        當影像辨識到物品或是臉部時,此方法會被執行
        @param detector_id: Detector id,用來識別為何種辨識結果
        @param image: 攝影機擷取到的照片
        @param data: 包含辨識結果,x,y軸
        """
        # id=1 當辨識到臉部表情時
        if detector_id == 1:
            labeledImage = image
            for result in data:
                label = result['emotion']
                labeledImage = visual_utils.annotateLabel(labeledImage, (result['x']['min'], result['y']['min']),
                                                          (result['x']['max'], result['y']['max']), label,
                                                          overwrite=False)

            # 顯示辨識結果視窗
            cv2.imshow("face", labeledImage)

            if time.time() <= self.face_timer:
                return

            obj: Optional[json] = face_db.queryForId(data[0]['emotion'])

            if obj is not None:
                data: json = obj['data']
                sendData = {"id": -1, "response_type": "json_object", "content": "single_object", "data": data}
                jsonString = json.dumps(sendData, ensure_ascii=False)
                print("Send:", jsonString)
                # 透過藍芽送出資料至互動介面
                self.commDevice.write(jsonString.encode(encoding='utf-8'))
                # 至少等待17秒才繼續進行影像辨識
                self.face_timer = time.time() + 17

        # id=2 當辨識到物品時
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
                # 取得最大機率之物品
                if result['conf'] > max_conf:
                    max_conf = result['conf']
                    max_index = i

                i = i + 1
            # 顯示辨識結果視窗
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
                # 透過藍芽送出資料至互動介面
                self.commDevice.write(jsonString.encode(encoding='utf-8'))
                # 至少等待17秒才繼續進行影像辨識
                self.object_timer = time.time() + 17


def formatDataToJsonString(id: int, type: str, content: str, data):
    sendData = {"id": id, "response_type": type, "content": content,
                "data": data}
    return json.dumps(sendData, ensure_ascii=False)


class MainProgram:
    def __init__(self):
        self.__id_counter = 0
        self._camera_monitor = CameraMonitor(0)

        # 初始化機器人
        robot = getDynamixel()
        robot.open()

        # 將機器人馬達扭力開啟
        for _id in robot.getAllServosId():
            robot.enableTorque(_id, True)
        self.robot = robot

    def initialize_server(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind(("0.0.0.0", 4444))
        server.listen(5)
        return server


    def main(self):
        # server = self.initialize_server()
        self._camera_monitor.registerDetector(FaceDetector(1), False)
        self._camera_monitor.registerDetector(ObjectDetector(2, conf=0.4), False)
        self._camera_monitor.start()

        while True:
            # client, address = server.accept()
            # print("Connected:", address)
            try:
                # commDevice = TCPCommDevice(client, EOLPackageHandler())
                commDevice=getSerialBluetooth()
                self._camera_monitor.setListener(MainCameraListener(commDevice))
                while True:
                    data = commDevice.read()
                    if data is None:
                        time.sleep(0.001)
                        continue
                    else:
                        command = data.decode(encoding='utf-8')
                        self.handleCommand(command, commDevice)
            except Exception as e:
                print(e.__str__())

    def handleCommand(self, cmd: str, commDevice: CommDevice):
        """
        當接收到互動介面所傳輸之指令時會被呼叫
        @param cmd:接收到之指令
        """

        print("receive:", cmd)

        if cmd == "DETECT_OBJECT" or cmd == "DETECT_INTER_OBJECT":
            # 開啟物品辨識Detector,關閉臉部辨識Detector
            self._camera_monitor.setDetectorEnable(1, False)
            self._camera_monitor.setDetectorEnable(2, True)

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
            commDevice.write(jsonString.encode(encoding='utf-8'))

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
                commDevice.write(jsonString.encode(encoding='utf-8'))
            elif l1.startswith("STORY"):
                # 送出指定故事之所有內容
                story_id = l1[6:]
                print("get story", story_id)
                story_content = stories_db.queryForId(story_id)
                jsonString = formatDataToJsonString(0, "json_object", "story_content", story_content['data'])
                print("Send:", jsonString)
                commDevice.write(jsonString.encode(encoding='utf-8'))
        elif cmd.startswith("DO_ACTION"):
            # 機器人做出動作 DO_ACTION [動作名稱].csv
            action = cmd[10:]
            # threading.Thread(target=doRobotAction, args=(action,)).start()
            self.doRobotAction(action)

        elif cmd == "STOP_ALL_ACTION":
            # 停止機器人所有動作
            pass
        elif cmd == "ALL_VOCABULARIES":
            # 送出所有單字資訊
            print("get all vocabulary")
            vocabularies_content = vocabularies_db.queryForId("vocabulary")
            print(vocabularies_content)
            jsonString = formatDataToJsonString(0, "json_array", "all_vocabularies", vocabularies_content['data'])
            print("Send:", jsonString)
            commDevice.write(jsonString.encode(encoding='utf-8'))

    def doRobotAction(self, csv_file):
        with open(csv_file, newline='') as file:
            rows = csv.reader(file, delimiter=",")
            line = 0
            for row in rows:
                if line == 0:
                    pass
                else:
                    if len(row) == 0:
                        continue

                    servoId = row[0]
                    position = row[1]
                    speed = row[2]
                    delay = row[3]

                    if not delay == '':
                        time.sleep(float(delay))

                    if servoId == '':
                        continue

                    if not speed == '':
                        self.robot.setVelocity(int(servoId), int(speed))

                    if not position == '':
                        self.robot.setGoalPosition(int(servoId), int(position))
                line = line + 1


if __name__ == '__main__':
    main = MainProgram()
    main.main()
