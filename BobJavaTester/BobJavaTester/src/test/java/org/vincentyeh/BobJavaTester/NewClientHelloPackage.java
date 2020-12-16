package org.vincentyeh.BobJavaTester;

import java.nio.charset.StandardCharsets;

public class NewClientHelloPackage extends NewPackage {

    private static final String VERIFY_UUID = "30b09b66-33d4-11eb-adc1-0242ac120002";
    private String UUID;
    
    public NewClientHelloPackage() {
        super(0xE0, setData());
        UUID=VERIFY_UUID;
    }

    public NewClientHelloPackage(byte[] importBytes) {
        super(importBytes);
        if (action != (byte) 0xE0)
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
