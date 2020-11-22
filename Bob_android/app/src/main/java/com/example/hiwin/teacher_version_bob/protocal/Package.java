package com.example.hiwin.teacher_version_bob.protocal;

import java.util.Arrays;

public abstract class Package {
    protected final byte action;
    protected byte[] data;
    protected byte length;

    public Package(byte action){
        this.action=action;
    }

    public Package(byte[] importBytes){
        if(importBytes[0]!=(byte)0xff||importBytes[1]!=(byte)0xef){
            throw new IllegalArgumentException("Header is not coincide.");
        }

        action=importBytes[2];
        length=importBytes[3];
        byte cksum = importBytes[importBytes.length - 1];

        byte cksum_real=(byte)(action+length);

        data=new byte[length];
        for(byte i=0;i<length;i++){
            data[i]=importBytes[4+i];
            cksum_real+=data[i];
        }
        cksum_real=(byte)(~cksum_real);

        if(cksum_real!= cksum){
            throw new IllegalArgumentException("Cksum is not coincide.");
        }
    }

    public byte[] toBytes(){
        byte[] package_data=new byte[5+length];
        byte cksum=0;
        package_data[0]=(byte)0xff;
        package_data[1]=(byte)0xef;
        package_data[2]=action;
        package_data[3]=length;
        cksum= (byte) (action+length);

        for(int i=0;i<length;i++){
            package_data[4+i]=data[i];
            cksum+=data[i];
        }
        cksum= (byte) ~cksum;
        package_data[package_data.length-1]=cksum;
        return package_data;
    }

}
