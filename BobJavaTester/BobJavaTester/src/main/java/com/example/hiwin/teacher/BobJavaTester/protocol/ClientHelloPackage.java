package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.nio.charset.StandardCharsets;

public class ClientHelloPackage extends Package {

    private static final String VERIFY_UUID = "30b09b66-33d4-11eb-adc1-0242ac120002";
    private String UUID;
    
    public ClientHelloPackage() {
        super(Package.Type.ClientHello.getAction(), setData());
        UUID=VERIFY_UUID;
    }

    public ClientHelloPackage(byte[] importBytes) {
        super(importBytes);
        if (action!=Package.Type.ClientHello.getAction())
            throw new IllegalArgumentException("Not a ClientHelloPackage");
        UUID = new String(getData(), StandardCharsets.UTF_8);
    }
    
    public boolean verify() {
    	return UUID.equals(VERIFY_UUID);
    }
    

    private static byte[] setData() {
        return VERIFY_UUID.getBytes(StandardCharsets.UTF_8);
    }


}
