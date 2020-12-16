package org.vincentyeh.BobJavaTester;

public class NewPackage {
	
    /**
     * 0<=action<=255
     */
    protected final int action;
    
    /**
     * 0<=length<=255
     */
    private final int length;
    
    
    /**
     * All element: -128~127
     */
    private final byte[] data;

    /**
     * @param action 0<=action<=255
     * @param data
     */
    public NewPackage(int action, byte[] data) {
    	if(action<0||action>255) {
    		throw new IllegalArgumentException("action:"+action);
    	}
    	
        this.action = action;
        this.data = data;
        this.length = data.length;
    }

    public NewPackage(byte[] importBytes) {
    	
        if (toUnsignedInt(importBytes[0])!=0xff || toUnsignedInt(importBytes[1])!=0xef) {
            throw new IllegalArgumentException("Header is not coincide.");
        }
        
        action = toUnsignedInt(importBytes[2]);
        length = toUnsignedInt(importBytes[3]);
        
        byte cksum = importBytes[importBytes.length - 1];
        
        int cksum_real = action + length;

        data = new byte[length];
        for (byte i = 0; i < length; i++) {
            data[i] = importBytes[4 + i];
            cksum_real += data[i];
        }
        
        if (cksum_real != toUnsignedInt(cksum)) {
            throw new IllegalArgumentException("Cksum is not coincide.");
        }
    }

    public byte[] toBytes() {
        byte[] package_data = new byte[5 + length];
        
        package_data[0] = (byte) 0xff;
        package_data[1] = (byte) 0xef;
        package_data[2] = (byte) action;
        package_data[3] = (byte) length;
        int cksum =action + length;

        for (int i = 0; i < length; i++) {
            package_data[4 + i] = data[i];
            cksum += data[i];
        }
        package_data[package_data.length - 1] = (byte)cksum;
        return package_data;
    }

    protected byte[] getData() {
        return data;
    }
    
    protected int toUnsignedInt(byte raw) {
    	return 0xff&raw;
    }
    
}
