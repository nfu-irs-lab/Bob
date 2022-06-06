from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from constants import detector, CommandControlListener
from device_config import getSocketBluetooth


class ConnectListener(ClientConnectionListener):

    def onConnected(self, socket):
        global monitor
        print("Monitor start")
        package_device = getSocketBluetooth(socket)
        package_device.setListener(CommandControlListener(package_device))
        package_device.start()


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
