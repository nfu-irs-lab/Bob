package com.example.hiwin.teacher_version_bob.data.framework.pack;

public abstract class Package{

    protected final byte[] content;

    public Package(Content<?> content) {
        this(content.getRaw());
    }

    public Package(Package pack) {
        this(pack.getEncoded());
    }

    protected Package(byte[] content) {
        this.content = content;
    }

    public abstract byte[] getEncoded();
    public abstract byte[] getDecoded();
}
