from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from Bob.visual.detector.concrete.face_detect_deepface import FaceDetector
from Bob.visual.detector.concrete.object_detect_yolov5 import ObjectDetector
from Bob.visual.monitor.concrete.crt_camera import CameraMonitor
from constants import CommandControlListener, MainCameraListener
from device_config import getSocketBluetooth


class ConnectListener(ClientConnectionListener):

    def __init__(self, camera_monitor: CameraMonitor):
        self.__camera_monitor = camera_monitor

    def onConnected(self, socket):
        print("Monitor start")
        package_device = getSocketBluetooth(socket)
        self.__camera_monitor.setListener(MainCameraListener(package_device))
        package_device.setListener(CommandControlListener(package_device, self.__camera_monitor))
        package_device.start()
        self.__camera_monitor.start()


class MainProgram:
    @staticmethod
    def main():
        camera_monitor = None
        server = None
        try:
            camera_monitor = CameraMonitor(0)
            camera_monitor.registerDetector(FaceDetector(1), False)
            camera_monitor.registerDetector(ObjectDetector(2, conf=0.4), False)
            server = BluetoothServer(ConnectListener(camera_monitor))
            server.start()

        except (KeyboardInterrupt, SystemExit):
            print("Interrupted!!")
        finally:
            if server is not None:
                server.close()
            if camera_monitor is not None:
                camera_monitor.stop()


if __name__ == '__main__':
    MainProgram.main()
