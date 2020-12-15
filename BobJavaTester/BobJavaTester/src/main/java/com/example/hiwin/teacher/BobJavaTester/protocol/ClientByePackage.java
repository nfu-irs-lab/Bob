package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.nio.charset.StandardCharsets;

public class ClientByePackage extends Package{

	
    public ClientByePackage() {
        super((byte) 0xE1, setData());
    }

    public ClientByePackage(byte[] importBytes) {
        super(importBytes);
        if (action != (byte) 0xE1)
            throw new IllegalArgumentException("Not a ClientByePackage");
    }
    
    private static byte[] setData() {
        return null;
    }


}
