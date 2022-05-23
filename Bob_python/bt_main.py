from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from Bob.communication.concrete.crt_strategy import ReadLineStrategy
from Bob.visual.camera.camera import CameraMonitor
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from constants import CommandControlListener, MainCameraListener
from device_config import getSocketBluetooth


class ConnectListener(ClientConnectionListener):

    def onConnected(self, socket):
        global monitor
        print("Monitor start")

        package_device = getSocketBluetooth(socket)
        monitor = package_device.getMonitor(CommandControlListener(package_device, camera_monitor), ReadLineStrategy())
        monitor.start()

        camera_monitor.setListener(MainCameraListener(package_device))
        camera_monitor.start()


camera_monitor = CameraMonitor()
camera_monitor.registerDetector(FaceDetector(1), False)
camera_monitor.registerDetector(ObjectDetector(2, conf=0.4), False)

try:
    server = BluetoothServer(ConnectListener())
    server.start()
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

server.close()
