package com.example.hiwin.teacher_version_bob.data.framework.pack;

public abstract class Package {

    private Package pack;
    private byte[] content;

    protected abstract byte[] encode(byte[] encoded);

    protected abstract byte[] decode(byte[] decoded);

    public Package(Package pack) {
        this.pack = pack;
    }

    public Package(byte[] bytes) {
        content = bytes;
    }

    public final byte[] getEncoded() {
        if (pack != null)
            return encode(pack.getEncoded());
        else if (content != null)
            return encode(content);
        else
            return null;
    }

    public final byte[] getDecoded() {
        if (pack != null)
            return decode(pack.getDecoded());
        else if (content != null) {
            return decode(content);
        }else
            return null;
    }

}
