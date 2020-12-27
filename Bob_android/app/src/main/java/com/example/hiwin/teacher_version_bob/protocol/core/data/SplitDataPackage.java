package com.example.hiwin.teacher_version_bob.protocol.core.data;


import com.example.hiwin.teacher_version_bob.protocol.core.Package;
import com.example.hiwin.teacher_version_bob.protocol.core.PackageHeader;
import com.example.hiwin.teacher_version_bob.protocol.core.PackageType;

public class SplitDataPackage extends Package {
    private final int index;
    private final int total;

    public SplitDataPackage(byte[] data, int index, int total) {
        super(PackageType.SplitData.getAction(), setData(data, index, total));
        this.index = index;
        this.total = total;
    }

    public SplitDataPackage(PackageHeader header, byte[] lackBytes) {
		super(header, lackBytes);
        if (action!=PackageType.SplitData.getAction())
            throw new IllegalArgumentException("Not a SplitDataPackage");
        
        this.index = super.getData()[0];
        this.total =  super.getData()[1];
    }
    
    
    public int getIndex() {
        return index;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public byte[] getData() {
        byte[] data= super.getData();
        byte[] newbytes = new byte[data.length - 2];
        for (int i = 2; i < data.length; i++) {
            newbytes[i-2] = data[i];
        }
        return newbytes;
    }

    private static byte[] setData(byte[] data, int index, int total) {
        byte[] newbytes = new byte[data.length + 2];
        newbytes[0] = (byte) index;
        newbytes[1] = (byte) total;
        for (int i = 2; i < newbytes.length; i++) {
            newbytes[i] = data[i - 2];
        }
        
        return newbytes;
    }

}
