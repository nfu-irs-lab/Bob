from Bob.device.concrete.crt_serial_dev import BluetoothSocketSerialDevice
from Bob.bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from Bob.communication.concrete.crt_package_device import SerialPackageDevice
from Bob.communication.concrete.crt_strategy import ReadLineStrategy
from constants import detector, CommandControlListener


class ConnectListener(ClientConnectionListener):

    def onConnected(self, socket):
        global monitor
        print("Monitor start")
        package_device = SerialPackageDevice(BluetoothSocketSerialDevice(socket, write_delay_ms=100))
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
