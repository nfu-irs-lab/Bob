import abc
from abc import ABC

import bluetooth


class ClientConnectionListener(ABC):
    @abc.abstractmethod
    def onConnected(self, socket):
        pass


class BluetoothServer:
    def __init__(self, listener: ClientConnectionListener):
        super().__init__()
        self.__run = True
        self.listener = listener
        server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
        server_sock.bind(("", bluetooth.PORT_ANY))
        server_sock.listen(1)

        port = server_sock.getsockname()[1]

        uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

        bluetooth.advertise_service(server_sock, "SampleServer", service_id=uuid,
                                    service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                                    profiles=[bluetooth.SERIAL_PORT_PROFILE],
                                    # protocols=[bluetooth.OBEX_UUID]
                                    )
        print("Waiting for connection on RFCOMM channel", port)
        self.server_sock = server_sock

    def start(self):
        while self.__run:
            client_sock, client_info = self.server_sock.accept()
            print("Accepted connection from", client_info)
            self.listener.onConnected(client_sock)

    def close(self):
        self.__run = False
