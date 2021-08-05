package com.example.hiwin.teacher_version_bob.data.concrete.pack;

import com.example.hiwin.teacher_version_bob.data.framework.pack.Content;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Package;

import java.nio.charset.Charset;

public class StringContent extends Content<String> {

    private final byte[] content;
    private final Charset charset;
    public StringContent(String string, Charset charset) {
        this(string.getBytes(charset),charset);
    }

    public StringContent(Package pack, Charset charset) {
        this(pack.getDecoded(),charset);
    }

    public StringContent(byte[] content, Charset charset) {
        this.content = content;
        this.charset = charset;
    }

    public String get(){
        return new String(content,charset);
    }

    @Override
    public byte[] getRaw() {
        return content;
    }

}
