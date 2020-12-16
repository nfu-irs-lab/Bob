import abc

VERIFY_UUID = "30b09b66-33d4-11eb-adc1-0242ac120002"


# class Protocol(metaclass=abc.ABCMeta):

class Package:
    def __init__(self, rawbytes=None, action=None, data=None):
        if action and data:
            self.data = data
            self.action = action
            self.length = len(data)
        elif rawbytes:
            if rawbytes[0] != 0xff or rawbytes[1] != 0xef:
                print("Header is not coincide")
            self.action = int(rawbytes[2])
            self.length = int(rawbytes[3])
            cksum = int(rawbytes[-1])
            self.data = rawbytes[4:-1]
            cksum_real = 0b11111111 & (self.action + self.length + sum(self.data))

            if cksum_real != cksum:
                print("Cksum is not coincide.")
        else:
            print("nothing...........")

    # def getPackage(data):
    #     if data[0] != 0xff or data[1] != 0xef:
    #         print("Header is not coincide")
    #     action = int(data[2])
    #     length = int(data[3])
    #     cksum = int(data[-1])
    #     newdata = data[4:-1]
    #
    #     cksum_real = 0b11111111 & (action + length + sum(newdata))
    #
    #     if cksum_real != cksum:
    #         print("Cksum is not coincide.")
    #     return Package(action=action, data=newdata)

    def toBytes(self):
        package_data = bytearray(5 + self.length)
        package_data[0] = 0xff
        package_data[1] = 0xef
        package_data[2] = self.action
        package_data[3] = self.length
        package_data[4:len(package_data) - 1] = self.data[0:]
        # byte
        cksum = self.action + self.length + sum(self.data)
        package_data[len(package_data) - 1] = 0b11111111 & cksum
        return package_data


class ClientHelloPackage(Package):
    def __init__(self, rawbytes=None):
        if rawbytes:
            super().__init__(action=0xe0, rawbytes=rawbytes)
            self.UUID = self.data.decode("UTF-8")
        else:
            self.UUID = VERIFY_UUID
            super().__init__(action=0xe0, data=self.UUID.encode("UTF-8"))

    def verify(self):
        return self.UUID == VERIFY_UUID
