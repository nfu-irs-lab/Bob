import unittest
import Protocol.protocol_module as pro


def dumpByteInHex(raw):
    print("[", raw.hex(" "), "]")


def test_package_varify_data():
    package1 = pro.Package(action=0x01, data=[1, 2, 3, 4, 5, 0xEF, 0xFF])
    dumpByteInHex(package1.toBytes())
    package2 = pro.Package(rawbytes=package1.toBytes())
    dumpByteInHex(package2.toBytes())


class MyTestCase(unittest.TestCase):

    def test_clienthello_Varify_data(self):
        package1 = pro.ClientHelloPackage()
        dumpByteInHex(package1.toBytes())
        package2 = pro.ClientHelloPackage(rawbytes=package1.toBytes())
        dumpByteInHex(package2.toBytes())

    def test_serverhello_Varify_data(self):
        package1 = pro.ServerHelloPackage(statusCode=0xF0)
        dumpByteInHex(package1.toBytes())
        package2 = pro.ServerHelloPackage(rawbytes=package1.toBytes())
        dumpByteInHex(package2.toBytes())
        assert package1.statusCode == package2.statusCode, "statusCode not same"

    def test_SplitDataPackage_varify(self):
        package1 = pro.SplitDataPackage(index=1, total=5, data=[1, 2, 3, 4, 5])
        dumpByteInHex(package1.toBytes())
        package2 = pro.SplitDataPackage(rawbytes=package1.toBytes())
        dumpByteInHex(package2.toBytes())

    def test_DataPackage(self):
        packages = pro.DataPackage.splitPackage([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        packages2 = pro.DataPackage()
        for package in packages:
            packages2.insert(package.index,package)
        print(packages2.getData())


if __name__ == '__main__':
    unittest.main()
