import uuid
from bluetooth import *
import RPi.GPIO as GPIO
 
LED_PIN = 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)
 
server_socket=BluetoothSocket(RFCOMM)
server_socket.bind(("", PORT_ANY))
server_socket.listen(1)
port = server_socket.getsockname()[1]
service_id = str(uuid.uuid4())
 
advertise_service(server_socket, "LEDServer",
                  service_id = service_id,
                  service_classes = [service_id, SERIAL_PORT_CLASS],
                  profiles = [SERIAL_PORT_PROFILE])
 
try:
    print('Ctrl-C')
    while True:
        print('RFCOMM '.format(port))
        client_socket, client_info = server_socket.accept()
        print('{}'.format(client_info))
        try:
            while True:
                data = client_socket.recv(1024).decode().lower()
                if len(data) == 0:
                    break
                if data == 'on':
                    GPIO.output(LED_PIN, GPIO.HIGH)
                    print('on')
                elif data == 'off':
                    GPIO.output(LED_PIN, GPIO.LOW)
                    print('off')
                else:
                    print(': {}'.format(data))
        except IOError:
            pass
        client_socket.close()
        print('disconnected')
except KeyboardInterrupt:
    print('stop')
finally:
    if 'client_socket' in vars():
        client_socket.close()
    server_socket.close()
    GPIO.cleanup()
    print('stop')
