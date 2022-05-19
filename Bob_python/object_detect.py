from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.detector.framework.detector import DetectListener


class AAListener(DetectListener):

    def onDetect(self, data):
        for obj in data:
            print(f'{obj["name"]}({round(obj["confidence"], 2)}),', end='')
        print()


detector = ObjectDetector(AAListener())
detector.start()
