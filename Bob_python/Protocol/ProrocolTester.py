import unittest
import Protocol.protocol_module as pro


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


class MyTestCase(unittest.TestCase):

    def test_clienthello(self):
        package1 = pro.ClientHelloPackage()
        rawbytes=package1.toBytes()
        print("header:")
        headerbytes=pro.Package.getPackageHeaderByteArray(rawbytes)
        dumpByteInHex(headerbytes)
        lackbytes=rawbytes[4:]
        print("lackbytes:")
        dumpByteInHex(lackbytes)
        header=pro.PackageHeader(headerBytes=headerbytes)
        package2=pro.ClientHelloPackage(header=header,lackBytes=lackbytes)


        print("package1:")
        dumpByteInHex(package1.toBytes())
        print("package2:")
        dumpByteInHex(package2.toBytes())


    def test_serverhello_Varify_data(self):
        pass
    def test_SplitDataPackage_varify(self):
        pass
    def test_DataPackage(self):
        pass
    # def test_pacjtest(self):
    #     package=pro.Package(action=0xe0,data=[1,2,3,4,0xee,0xff])
    #     dumpByteInHex(package.toBytes())
    #     dumpByteInHex(package.toBytes()[0:4])
    #     dumpByteInHex(package.toBytes()[4:])
    #     header=pro.PackageHeader(header=package.toBytes()[0:4])
    #     package2=pro.Package(header=header,lackBytes=package.toBytes()[4:])
    #     dumpByteInHex(package2.toBytes())
    # def test_type(self):
    #     print(pro.PackageType.SplitData.value)
    # def test_header(self):
    #     pro.PackageHeader(action=255,length=5)

if __name__ == '__main__':
    unittest.main()
