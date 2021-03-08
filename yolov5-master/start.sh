sudo chmod a+rwx /dev/ttyUSB0
sudo chmod a+rwx /dev/ttyUSB1
python3 detect_bluetooth_func3.py --source 0 --weight yolov5s.pt
