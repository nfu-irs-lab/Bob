from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from Bob.communication.concrete.crt_strategy import ReadLineStrategy
from constants import detector, CommandControlListener
from device_config import getSocketBluetooth


class ConnectListener(ClientConnectionListener):

    def onConnected(self, socket):
        global monitor
        print("Monitor start")
        package_device = getSocketBluetooth(socket)
        monitor = package_device.getMonitor(CommandControlListener(package_device), ReadLineStrategy())
        monitor.start()


try:
    server = BluetoothServer(ConnectListener())
    server.start()
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

server.close()

if detector is not None:
    detector.stop()

if monitor is not None:
    monitor.stop()
