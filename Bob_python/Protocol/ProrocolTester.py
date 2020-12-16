import unittest
import Protocol.protocol_module as pro
def dumpByteInHex(raw):
    print("[",raw.hex(" "),"]")
class MyTestCase(unittest.TestCase):

    def test_package_varify_data(self):
        package1 = pro.Package(action=0x01,data=[1, 2, 3, 4, 5, 0xEF, 0xFF])
        dumpByteInHex(package1.toBytes())
        package2 = pro.Package(rawbytes=package1.toBytes())
        dumpByteInHex(package2.toBytes())
    def test_clienthello_Varify_data(self):
        package1=pro.ClientHelloPackage()
        dumpByteInHex(package1.toBytes())
        package2=pro.ClientHelloPackage(rawbytes=package1.toBytes())
        dumpByteInHex(package2.toBytes())


if __name__ == '__main__':
    unittest.main()
