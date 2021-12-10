"""Run inference with a YOLOv5 model on images, videos, directories, streams

Usage:
    $ python path/to/object_detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""

import time
from communication.concrete.crt_strategy import ReadLineStrategy
from constants import bt_description, CommandControlListener, detector
from serial_utils import getBluetoothPackageDeviceWithDescription

package_device = getBluetoothPackageDeviceWithDescription(bt_description)
monitor = package_device.getMonitor(CommandControlListener(package_device), ReadLineStrategy())
monitor.start()

print("Monitor start")
try:
    while True:
        time.sleep(1)
except (KeyboardInterrupt, SystemExit):
    print("Interrupted!!")

if detector is not None:
    detector.stop()
monitor.stop()
package_device.close()
