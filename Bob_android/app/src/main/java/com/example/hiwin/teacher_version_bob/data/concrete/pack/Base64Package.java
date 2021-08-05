package com.example.hiwin.teacher_version_bob.data.concrete.pack;

import android.util.Base64;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Content;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Package;


public class Base64Package extends Package {

    private final int base64EncodeFlag;

    public Base64Package(Content<?> content, int base64EncodeFlag) {
        super(content.getRaw());
        this.base64EncodeFlag = base64EncodeFlag;
    }

    public Base64Package(Package pack, int base64EncodeFlag) {
        super(pack);
        this.base64EncodeFlag = base64EncodeFlag;
    }

    public Base64Package(byte[] content, int base64EncodeFlag) {
        super(content);
        this.base64EncodeFlag = base64EncodeFlag;
    }

    @Override
    public byte[] getEncoded() {
        return Base64.encode(content, base64EncodeFlag);
    }

    @Override
    public byte[] getDecoded() {
        return Base64.decode(content, base64EncodeFlag);
    }
}
