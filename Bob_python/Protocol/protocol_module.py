from enum import Enum

VERIFY_UUID = "30b09b66-33d4-11eb-adc1-0242ac120002"
SPLIT_SIZE = 253


# class Protocol(metaclass=abc.ABCMeta):

class Package:
    def __init__(self, rawbytes=None, action=None, data=None):
        if action and data:
            self._data = data
            self.action = action
            self.length = len(data)
        elif rawbytes:
            if rawbytes[0] != 0xff or rawbytes[1] != 0xef:
                print("Header is not coincide")
            self.action = int(rawbytes[2])
            self.length = int(rawbytes[3])
            cksum = int(rawbytes[-1])
            self._data = rawbytes[4:-1]
            cksum_real = 0b11111111 & (self.action + self.length + sum(self._data))

            if cksum_real != cksum:
                print("Cksum is not coincide.")
        else:
            print("nothing...........")

    def toBytes(self):
        package_data = bytearray(5 + self.length)
        package_data[0] = 0xff
        package_data[1] = 0xef
        package_data[2] = self.action
        package_data[3] = self.length
        package_data[4:len(package_data) - 1] = self._data[0:]
        # byte
        cksum = self.action + self.length + sum(self._data)
        package_data[len(package_data) - 1] = 0b11111111 & cksum
        return package_data


class ClientHelloPackage(Package):
    def __init__(self, rawbytes=None):
        if rawbytes:
            super().__init__(rawbytes=rawbytes)
            self.UUID = self._data.decode("UTF-8")
            if self.action != 0xe0:
                print("Not a ClientHelloPackage")

        else:
            self.UUID = VERIFY_UUID
            super().__init__(action=0xe0, data=self.UUID.encode("UTF-8"))

    def verify(self):
        return self.UUID == VERIFY_UUID


class ServerHelloPackage(Package):
    def __init__(self, rawbytes=None, statusCode=None):
        if rawbytes:
            super().__init__(rawbytes=rawbytes)
            if self.action != 0xf0:
                print("Not a ServerHelloPackage")
            self.statusCode = self._data[0]
        else:
            self.statusCode = statusCode
            buf = bytearray(1)
            buf[0] = self.statusCode
            super().__init__(action=0xf0, data=buf)


class SplitDataPackage(Package):
    def __init__(self, data=None, index=None, total=None, rawbytes=None):
        if rawbytes:
            super(SplitDataPackage, self).__init__(rawbytes=rawbytes)
            self.index = self._data[0]
            self.total = self._data[1]
        else:
            self.index = index
            self.total = total
            newbytes = bytearray(len(data) + 2)
            newbytes[0] = index
            newbytes[1] = total
            newbytes[2:] = data[0:]
            super(SplitDataPackage, self).__init__(action=0x03, data=newbytes)

    def getData(self):
        data = self._data
        newbytes = bytearray(len(data) - 2)
        newbytes[:] = data[2:len(data)]
        return newbytes


class DataPackage():
    def __init__(self):
        self._dataPackages = []

    def splitPackage(data):
        dataPackages = []
        availableBytes = len(data)
        index = 0
        counter = 0
        total = int(len(data) / SPLIT_SIZE) + len(data) % SPLIT_SIZE

        while availableBytes > 0:
            length = min(availableBytes, SPLIT_SIZE)
            splited = DataPackage.splitBytes(data, index, length)
            splitDataPackage = SplitDataPackage(data=splited, index=counter, total=total)
            dataPackages.insert(splitDataPackage.index, splitDataPackage)
            availableBytes = availableBytes - SPLIT_SIZE
            index = index + SPLIT_SIZE
            counter = counter + 1

        return dataPackages

    def splitBytes(raw, start, length):
        endIndex = start + length - 1
        if endIndex > len(raw) - 1:
            print("Index out of bound")

        splited = bytearray(length)
        splited[0:] = raw[start:endIndex + 1]
        return splited

    def getData(self):
        newData = []
        for splitDataPackage in self._dataPackages:
            splitBytes = splitDataPackage.getData()
            for b in splitBytes:
                newData.append(b)
        newBytes = bytearray(len(newData))
        for i in range(0, len(newData)):
            newBytes[i] = newData[i]
        return newBytes

    # array----------------------------------------------
    def append(self, data):
        self._dataPackages.append(data)

    def insert(self, index, data):
        self._dataPackages.insert(index, data)

    def clear(self):
        self._dataPackages.clear()

    def get(self, index):
        return self._dataPackages[index]

    # array----------------------------------------------


class PackageType(Enum):
    ServerHello = 0xf0
    ClientHello = 0xe0
    SplitData = 0x03

    @staticmethod
    def values():
        return list(PackageType)


    @staticmethod
    def getPackageType(rawbytes):
        if rawbytes[0]!=0xff or rawbytes[1]!=0xef:
            print("Header is not coincide.")
        action=rawbytes[2]
        types=PackageType.values()
        for type in types:
            if action==type.value:
                return type

        return None