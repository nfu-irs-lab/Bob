package com.example.hiwin.teacher_version_bob.data.concrete.pack;

import android.util.Base64;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Package;


public class Base64Package extends Package {

    private final int base64EncodeFlag;

    public Base64Package(byte[] data,int base64EncodeFlag){
        super(data);
        this.base64EncodeFlag = base64EncodeFlag;
    }

    @Override
    protected byte[] encode(byte[] encoded) {
        return Base64.encode(encoded, base64EncodeFlag);
    }

    @Override
    protected byte[] decode(byte[] decoded) {
        return Base64.decode(decoded, base64EncodeFlag);
    }

}
