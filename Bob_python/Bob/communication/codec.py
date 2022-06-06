import base64


class PackageCodec:

    @staticmethod
    def encode(raw: bytes) -> bytes:
        byte_array = bytearray(base64.b64encode(raw))
        byte_array.append("\n".encode()[0])
        return bytes(byte_array)

    @staticmethod
    def encodeString(string: str) -> bytes:
        return PackageCodec.encode(string.encode(encoding='utf-8'))

    @staticmethod
    def decode(raw: bytes, hasEOL: bool) -> bytes:
        if hasEOL:
            return base64.b64decode(raw[0:-1])
        else:
            return base64.b64decode(raw)
