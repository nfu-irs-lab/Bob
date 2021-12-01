import base64

from communication.framework.fw_package import Package


class Base64LinePackage(Package):

    def __init__(self, package: Package):
        self.package = package

    def getBytes(self) -> bytes:
        byte_array = bytearray(base64.b64encode(self.package.getBytes()))
        byte_array.append("\n".encode()[0])
        return bytes(byte_array)


class StringPackage(Package):
    def __init__(self, content: str, charset: str):
        self.bytes_ = content.encode(charset)

    def getBytes(self) -> bytes:
        return self.bytes_
