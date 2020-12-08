package com.example.hiwin.teacher_version_bob.protocol;

public class ServerHelloPackage extends Package {

    public static enum StatusCode{
        ALLOW((byte)0xFF),NOT_SUPPORT((byte)0x01),DENY((byte)0x00);

        public final byte code;
        StatusCode(byte code){
            this.code=code;
        }

        public byte getCode() {
            return code;
        }

        public static StatusCode getStatus(byte code){
            switch (code){
                case (byte)0xff:
                    return ALLOW;
                case (byte)0x01:
                    return NOT_SUPPORT;
                case (byte)0x00:
                    return DENY;
                default:
                    throw new IllegalArgumentException(code+" is not a status code.");
            }
        }
        
    }

    private final StatusCode statusCode;
    public ServerHelloPackage(byte[] importBytes) {
        super(importBytes);
        if(action!=(byte)0xF0)
            throw new IllegalArgumentException("Not a ServerHelloPackage");
        statusCode=StatusCode.getStatus(getData()[0]);

    }
    public ServerHelloPackage(StatusCode statusCode){
        super((byte)0XF0,setData(statusCode));
        this.statusCode=statusCode;
    }

    private static byte[] setData(StatusCode code){
        return new byte[]{code.getCode()};
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
