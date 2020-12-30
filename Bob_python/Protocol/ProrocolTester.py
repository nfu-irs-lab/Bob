import unittest
import Protocol.protocol_module as pro


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


def compareBytes(a: bytes, b: bytes) -> bool:
    if len(a) != len(b):
        return False
    for i in range(0, len(a)):
        if a[i] != b[i]:
            return False

    return True


class MainListener(pro.ProtocolListener):
    def OnProtocolConnected(self):
        pass

    def OnProtocolDisconnected(self):
        pass

    def OnReceiveDataPackage(self, data: bytes):
        pass

    def OnWrite(self, data: bytes):
        print("[Write]")
        dumpByteInHex(data)


class MyTestCase(unittest.TestCase):

    def test_clienthello(self):
        package1 = pro.ClientHelloPackage()
        rawbytes = package1.toBytes()
        print("header:")
        headerbytes = pro.Package.getPackageHeaderByteArray(rawbytes)
        dumpByteInHex(headerbytes)
        lackbytes = rawbytes[4:]
        print("lackbytes:")
        dumpByteInHex(lackbytes)
        header = pro.PackageHeader(headerBytes=headerbytes)
        package2 = pro.ClientHelloPackage(header=header, lackBytes=lackbytes)

        print("package1:")
        dumpByteInHex(package1.toBytes())
        print("package2:")
        dumpByteInHex(package2.toBytes())
        self.assertTrue(compareBytes(package1.toBytes(), package2.toBytes()), "package1!=package2")

    def test_serverhello(self):
        print("[ServerHelloPackage]")
        package1 = pro.ServerHelloPackage(statusCode=pro.StatusCode.ALLOW)
        rawbytes = package1.toBytes()
        print("header:")
        headerbytes = pro.Package.getPackageHeaderByteArray(rawbytes)
        dumpByteInHex(headerbytes)
        lackbytes = rawbytes[4:]
        print("lackbytes:")
        dumpByteInHex(lackbytes)
        header = pro.PackageHeader(headerBytes=headerbytes)
        package2 = pro.ServerHelloPackage(header=header, lackBytes=lackbytes)

        print("package1:")
        print("\tstatus:", package1.statusCode)
        dumpByteInHex(package1.toBytes())
        print("package2:")
        print("\tstatus:", package2.statusCode)
        dumpByteInHex(package2.toBytes())

        type = pro.PackageType.getPackageType(header)
        self.assertEqual(type, pro.PackageType.ServerHello, "type!=ServerHello")
        self.assertTrue(compareBytes(package1.toBytes(), package2.toBytes()), "package1!=package2")

    def test_DataPackage(self):
        pass

    def test_clientProtocolSocket(self):
        socket = pro.ClientProtocolSocket()
        socket.sendHello()
        socket.attach(MainListener())
        a = bytearray(5)
        for i in range(1, 6):
            a[i - 1] = i
        socket.writeBytes(a)

    def test_serverProtocolSocket(self):
        socket = pro.ClientProtocolSocket()
        socket.sendHello()
        socket.attach(MainListener())
        a = bytearray(5)
        for i in range(1, 6):
            a[i - 1] = i
        socket.writeBytes(a)

    # def test_pacjtest(self):
    #     package = pro.Package(action=0xe0, data=[1, 2, 3, 4, 0xee, 0xff])
    #     dumpByteInHex(package.toBytes())
    #     dumpByteInHex(package.toBytes()[0:4])
    #     dumpByteInHex(package.toBytes()[4:])
    #     header = pro.PackageHeader(headerBytes=package.toBytes()[0:4])
    #     package2 = pro.Package(header=header, lackBytes=package.toBytes()[4:])
    #     dumpByteInHex(package2.toBytes())
    #
    # def test_type(self):
    #     print(pro.PackageType.SplitData.value)
    #
    # def test_header(self):
    #     pro.PackageHeader(action=255, length=5)


if __name__ == '__main__':
    unittest.main()
