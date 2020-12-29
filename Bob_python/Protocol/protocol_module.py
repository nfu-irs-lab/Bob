from enum import Enum
import abc

VERIFY_UUID = "30b09b66-33d4-11eb-adc1-0242ac120002"
SPLIT_SIZE = 253


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


class PackageHeader:
    def __init__(self, action: int = None, length: int = None, headerBytes: bytes = None):
        if action and length:
            if action < 0 or action > 255:
                raise RuntimeError("action error")
            self.action = action
            self.length = length
            self.lackBytesLength = length + 1
        elif headerBytes:
            if len(headerBytes) > 4 or len(headerBytes) <= 0:
                raise RuntimeError("Header size is incorrect:" + str(len(headerBytes)))

            if headerBytes[0] != 0xff or headerBytes[1] != 0xef:
                raise RuntimeError("Header is not coincide.")

            self.action = headerBytes[2]
            self.length = headerBytes[3]
            self.lackBytesLength = self.length + 1

    # def createHeaderByteArray(self):
    #     package_data = bytearray(4 + self.lackBytesLength)
    #     package_data[0] = 0xff
    #     package_data[1] = 0xef
    #     package_data[2] = self.action
    #     package_data[3] = self.length
    #     return package_data

    # @staticmethod
    # def getPackageHeader(rawbytes):
    #     header = rawbytes[0:4]
    #     return header

    def getHeader(self):
        return self


# class Protocol(metaclass=abc.ABCMeta):
class Package(PackageHeader):
    def __init__(self, header: PackageHeader = None, lackBytes: bytearray or bytes = None, action: int = None,
                 data: bytearray or bytes = None):
        if action and data:
            super(Package, self).__init__(action=action, length=len(data))
            self._data = data
        elif header and lackBytes:
            super(Package, self).__init__(action=header.action, length=header.length)
            self._data = bytearray(self.length)
            cksum = int(lackBytes[-1])
            self._data = lackBytes[0:-1]

            cksum_real = 0b11111111 & (self.action + self.length + sum(self._data))
            if cksum_real != cksum:
                raise RuntimeError("Cksum is not coincide.")
        else:
            raise RuntimeError("Nothing was done when initialize Package class")

    def toBytes(self) -> bytes:
        package_data = self.createEmptyPackageByteArray()
        package_data[4:-1] = self._data
        cksum = self.action + self.length + sum(self._data)
        package_data[len(package_data) - 1] = 0b11111111 & cksum
        return package_data

    def createEmptyPackageByteArray(self):
        package_data = bytearray(4 + self.lackBytesLength)
        package_data[0] = 0xff
        package_data[1] = 0xef
        package_data[2] = self.action
        package_data[3] = self.length
        return package_data

    @staticmethod
    def getPackageHeaderByteArray(rawbytes: bytes):
        return rawbytes[0:4]


class PackageType(Enum):
    ClientHello = 0xe0
    ServerHello = 0xf0
    ClientBye = 0xe1
    SplitData = 0x03
    VerifyResponse = 0x04

    @staticmethod
    def values() -> list:
        return list(PackageType)

    @staticmethod
    def getPackageTypeFormBytes(rawbytes: bytearray):
        header_bytes = Package.getPackageHeaderByteArray(rawbytes)
        try:
            header = PackageHeader(header_bytes)
            return PackageType.getPackageType(header)
        except:
            return None

    @staticmethod
    def getPackageType(header: PackageHeader):
        types = PackageType.values()
        for type in types:
            if header.action == type.value:
                return type


# ############################################################
class ClientHelloPackage(Package):
    # def __init__(self, rawbytes=None):
    #     if rawbytes:
    #         super().__init__(rawbytes=rawbytes)
    #         self.UUID = self._data.decode("UTF-8")
    #         if self.action != 0xe0:
    #             print("Not a ClientHelloPackage")
    #
    #     else:
    #         self.UUID = VERIFY_UUID
    #         super().__init__(action=0xe0, data=self.UUID.encode("UTF-8"))

    def __init__(self, header: PackageHeader = None, lackBytes=None):
        if header and lackBytes:
            super().__init__(header=header, lackBytes=lackBytes)
            if self.action != PackageType.ClientHello.value:
                raise RuntimeError("Not a ClientHelloPackage")
            self.UUID = self._data.decode("UTF-8")
        else:
            super().__init__(action=PackageType.ClientHello.value, data=self.__setData())
            self.UUID = VERIFY_UUID

    def verify(self):
        return self.UUID == VERIFY_UUID

    @staticmethod
    def __setData() -> bytes:
        return VERIFY_UUID.encode("UTF-8")


class ServerHelloPackage(Package):
    # def __init__(self, rawbytes=None, statusCode=None):
    #     if rawbytes:
    #         super().__init__(rawbytes=rawbytes)
    #         if self.action != 0xf0:
    #             print("Not a ServerHelloPackage")
    #         self.statusCode = self._data[0]
    #     else:
    #         self.statusCode = statusCode
    #         buf = bytearray(1)
    #         buf[0] = self.statusCode
    #         super().__init__(action=0xf0, data=buf)

    def __init__(self, header: PackageHeader = None, lackBytes=None, statusCode=None):
        if header and lackBytes:
            super().__init__(header=header, lackBytes=lackBytes)
            if self.action != PackageType.ServerHello.value:
                raise RuntimeError("Not a ServerHelloPackage")
            self.statusCode = StatusCode.getStatus(self._data[0])
        elif statusCode:
            self.statusCode = statusCode
            super().__init__(action=PackageType.ServerHello.value, data=self.__setData(self.statusCode))

    @staticmethod
    def __setData(statusCode) -> bytearray:
        datas = bytearray(1)
        datas[0] = statusCode.value
        return datas


class SplitDataPackage(Package):
    # def __init__(self, data=None, index=None, total=None, rawbytes=None):
    #     if rawbytes:
    #         super(SplitDataPackage, self).__init__(rawbytes=rawbytes)
    #         self.index = self._data[0]
    #         self.total = self._data[1]
    #     else:
    #         self.index = index
    #         self.total = total
    #         newbytes = bytearray(len(data) + 2)
    #         newbytes[0] = index
    #         newbytes[1] = total
    #         newbytes[2:] = data[0:]
    #         super(SplitDataPackage, self).__init__(action=0x03, data=newbytes)

    def __init__(self, header: PackageHeader = None, lackBytes=None, data=None, index: int = None, total: int = None):
        if header and lackBytes:
            super().__init__(header=header, lackBytes=lackBytes)
            if self.action != PackageType.SplitData.value:
                raise RuntimeError("Not a SplitDataPackage")

            # Warning!!
            self.index = int(self._data[0])
            self.total = int(self._data[1])

        else:
            super().__init__(action=0x03, data=self.__setData(data=data, index=index, total=total))
            self.index = index
            self.total = total

    @staticmethod
    def __setData(data, index, total) -> bytearray:
        newbytes = bytearray(len(data) + 2)
        newbytes[0] = index
        newbytes[1] = total
        newbytes[2:] = data[0:]
        return newbytes

    def getData(self):
        data = self._data
        newbytes = bytearray(len(data) - 2)
        newbytes[:] = data[2:len(data)]
        return newbytes


class DataPackage():
    def __init__(self, total: int = None):
        if total:
            self.total = total
            self.__packages = []
            self.received_counter = 0
            for i in range(0, total):
                self.__packages.append(None)

    def receive(self, splitDataPackage: SplitDataPackage):

        self.__packages[splitDataPackage.index] = splitDataPackage
        self.received_counter = self.received_counter + 1

    def isComplete(self):
        counter1 = 0
        for i in range(0, self.total):
            counter1 = counter1 + 1

        counter2 = 0
        for splitDataPackage in self.__packages:
            if not splitDataPackage:
                continue
            counter2 = counter2 + splitDataPackage.index

        return counter1 == counter2

    def receivedPackages(self):
        return self.received_counter

    def getData(self):
        if not self.isComplete():
            return None

        newData = []
        for splitDataPackage in self.__packages:
            splitBytes = splitDataPackage.getData()
            for b in splitBytes:
                newData.append(b)
        newBytes = bytearray(len(newData))
        for i in range(0, len(newData)):
            newBytes[i] = newData[i]
        return newBytes

    @staticmethod
    def __splitBytes(raw: bytearray, start: int, length: int) -> bytes:
        endIndex = start + length - 1
        if endIndex > len(raw) - 1:
            raise RuntimeError("Index out of bound")

        splited = bytearray(length)
        splited[0:] = raw[start:endIndex + 1]
        return splited

    @staticmethod
    def splitPackage(data: bytearray or bytes) -> list:
        dataPackages = []
        availableBytes = len(data)
        index = 0
        counter = 0
        total = int(availableBytes / SPLIT_SIZE) + min(1, availableBytes % SPLIT_SIZE)

        while availableBytes > 0:
            length = min(availableBytes, SPLIT_SIZE)
            splited = DataPackage.__splitBytes(data, index, length)
            splitDataPackage = SplitDataPackage(data=splited, index=counter, total=total)
            dataPackages.insert(splitDataPackage.index, splitDataPackage)
            availableBytes = availableBytes - SPLIT_SIZE
            index = index + SPLIT_SIZE
            counter = counter + 1
        return dataPackages


class StatusCode(Enum):
    ALLOW = 0xff
    NOT_SUPPORT = 0x01
    DENY = 0x00

    @staticmethod
    def values():
        return list(StatusCode)

    @staticmethod
    def getStatus(code: int):
        status_list = StatusCode.values()
        for status in status_list:
            if code == status.value:
                return status
        raise RuntimeError(str(code) + " is not a status code.")

    # @staticmethod
    # def getStatus(rawbytes):
    #     if rawbytes[0] != 0xff or rawbytes[1] != 0xef:
    #         print("Header is not coincide.")
    #     code = rawbytes[2]
    #     status_list = StatusCode.values()
    #     for status in status_list:
    #         if code == status.value:
    #             return status
    #     return None


class ProtocolListener(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def OnProtocolConnected(self):
        pass

    @abc.abstractmethod
    def OnProtocolDisconnected(self):
        pass

    @abc.abstractmethod
    def OnReceiveDataPackage(self, data: bytes):
        pass

    @abc.abstractmethod
    def OnWrite(self, data: bytes):
        pass


WRITE_DATA_RATE = 5000


class ConnectionStatus(Enum):
    Connected = 1
    Disconnected = 0


class ProtocolSocket(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def received(self, header_bytes: bytes, lack_bytes: bytes):
        pass

    def __init__(self):
        self.status = None
        self.pro_listener = None
        self.dataPackages = None

    def attach(self, listener: ProtocolListener):
        self.pro_listener = listener

    def onSplitDataReceive(self, splitDataPackage: SplitDataPackage):
        if splitDataPackage.index == 0:
            self.dataPackages = DataPackage(total=splitDataPackage.total)

        if self.dataPackages:
            if self.dataPackages.receivedPackages() == 0 and splitDataPackage.index != 0:
                return
            if self.dataPackages.isComplete():
                print("Complete")
                if self.pro_listener:
                    self.pro_listener.OnReceiveDataPackage(data=self.dataPackages.getData())
                self.dataPackages = None

    def writeBytes(self, data: bytes):
        if not self.isConnected():
            return
        dataPackages = DataPackage.splitPackage(data=data)
        for package in dataPackages:
            self.__writePackage(package)

    def __writePackage(self, package: Package):
        print("[Write Data]")
        dumpByteInHex(package.toBytes())

        if self.pro_listener:
            self.pro_listener.OnWrite(data=package.toBytes())

    def close(self):
        self.status = ConnectionStatus.Disconnected
        if self.pro_listener:
            self.pro_listener.OnProtocolDisconnected()

    def isConnected(self):
        return self.status == ConnectionStatus.Connected


class ClientProtocolSocket(ProtocolSocket):

    def received(self, header_bytes: bytes, lack_bytes: bytes):
        header = PackageHeader(headerBytes=header_bytes)
        type = PackageType.getPackageType(header)
        print("Type:")
        print(type)

        if type == PackageType.ServerHello:
            serverHelloPackage = ServerHelloPackage(header=header, lackBytes=lack_bytes)

            if serverHelloPackage.statusCode == StatusCode.ALLOW:
                self.status = ConnectionStatus.Connected
            else:
                self.status = ConnectionStatus.Disconnected
        elif type == PackageType.SplitData:
            try:
                splitDataPackage = SplitDataPackage(header=header, lackBytes=lack_bytes)
                self.onSplitDataReceive(splitDataPackage)
            except:
                print("Error")

    def connect(self):
        self.sendHello()

    def sendHello(self):
        self.__writePackage(ClientHelloPackage())
