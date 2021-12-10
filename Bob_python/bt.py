import base64

from Bob.device.concrete.crt_serial_dev import BluetoothSocketSerialDevice
from bluetooth_utils.utils import BluetoothServer, ClientConnectionListener
from communication.concrete.crt_monitor import ReadLineStrategy
from communication.concrete.crt_package import StringPackage
from communication.concrete.crt_package_device import SerialPackageDevice
from communication.framework.fw_monitor import SerialListener
from communication.framework.fw_package_device import PackageDevice


class AListener(SerialListener):

    def __init__(self, device: PackageDevice):
        self.pk_dev = device

    def onReceive(self, data: bytes):
        d = base64.decodebytes(data)
        print(d.decode())
        self.pk_dev.writePackage(StringPackage(d.decode(), 'UTF-8'))


monitor = None


class BListener(ClientConnectionListener):

    def onConnected(self, socket):
        global monitor
        pk_dev = SerialPackageDevice(BluetoothSocketSerialDevice(socket))
        monitor = pk_dev.getMonitor(AListener(pk_dev), ReadLineStrategy())
        monitor.start()


try:
    server = BluetoothServer(BListener())
    server.start()
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

monitor.stop()
server.close()

# monitor.stop()
