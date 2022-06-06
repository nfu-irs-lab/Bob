from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.monitor.concrete.crt_camera import CameraMonitor
from constants import CommandControlListener, MainCameraListener
from device_config import getSocketBluetooth


class ConnectListener(ClientConnectionListener):

    def onConnected(self, socket):
        print("Monitor start")
        package_device = getSocketBluetooth(socket)
        camera_monitor.setListener(MainCameraListener(package_device))
        package_device.setListener(CommandControlListener(package_device,camera_monitor))
        package_device.start()
        camera_monitor.start()


camera_monitor = CameraMonitor(0)
camera_monitor.registerDetector(FaceDetector(1), False)
camera_monitor.registerDetector(ObjectDetector(2, conf=0.4), False)

try:
    server = BluetoothServer(ConnectListener())
    server.start()
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

server.close()
