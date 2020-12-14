package com.example.hiwin.teacher_version_bob.protocol;

public abstract class Package {
    public static enum Type {
        ClientHello, ServerHello, ClientBye, Message;

        public static Type getPackageType(byte[] importBytes) {
            if (importBytes[0] != (byte) 0xff || importBytes[1] != (byte) 0xef) {
                throw new IllegalArgumentException("Header is not coincide.");
            }

            byte action = importBytes[2];
            switch (action) {
                case (byte) 0xF0:
                    return ServerHello;
                case (byte) 0xE0:
                    return ClientHello;
                case (byte) 0xE1:
                    return ClientBye;
                case (byte) 0x01:
                    return Message;
                default:
                    return null;

            }
        }

    }

    protected final byte action;
    private final byte length;
    private final byte[] data;

    public Package(byte action, byte[] data) {
        this.action = action;
        this.data = data;
        this.length = (byte) data.length;
    }

    public Package(byte[] importBytes) {
        if (importBytes[0] != (byte) 0xff || importBytes[1] != (byte) 0xef) {
            throw new IllegalArgumentException("Header is not coincide.");
        }

        action = importBytes[2];
        length = importBytes[3];
        byte cksum = importBytes[importBytes.length - 1];

        byte cksum_real = (byte) (action + length);

        data = new byte[length];
        for (byte i = 0; i < length; i++) {
            data[i] = importBytes[4 + i];
            cksum_real += data[i];
        }
        cksum_real = (byte) (~cksum_real);

        if (cksum_real != cksum) {
            throw new IllegalArgumentException("Cksum is not coincide.");
        }
    }

    public byte[] toBytes() {
        byte[] package_data = new byte[5 + length];
        byte cksum = 0;
        package_data[0] = (byte) 0xff;
        package_data[1] = (byte) 0xef;
        package_data[2] = action;
        package_data[3] = (byte) length;
        cksum = (byte) (action + length);

        for (int i = 0; i < length; i++) {
            package_data[4 + i] = data[i];
            cksum += data[i];
        }
        cksum = (byte) ~cksum;
        package_data[package_data.length - 1] = cksum;
        return package_data;
    }

    protected byte[] getData() {
        return data;
    }

//    public static Object parsePackage(byte[] importBytes) {
//        if (importBytes[0] != (byte) 0xff || importBytes[1] != (byte) 0xef) {
//            throw new IllegalArgumentException("Header is not coincide.");
//        }
//
//        byte action = importBytes[2];
//        switch (action) {
//            case (byte) 0xF0:
//                return new ServerHelloPackage(importBytes);
//            case (byte) 0xE0:
//                return new ClientHelloPackage(importBytes);
//
//            default:
//                return null;
//
//        }
//
//    }

}
