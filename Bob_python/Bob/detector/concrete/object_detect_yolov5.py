import yolov5.detect
from Bob.detector.framework.detector import Detector, DetectListener


class ObjectDetector(Detector):

    def __init__(self, listener: DetectListener):
        super().__init__(listener)
        self.listener = listener

    def _detect(self):
        yolov5.detect.run(source=0, listener=self.listener)
