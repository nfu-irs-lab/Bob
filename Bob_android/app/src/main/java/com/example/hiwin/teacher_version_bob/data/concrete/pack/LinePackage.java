package com.example.hiwin.teacher_version_bob.data.concrete.pack;

import com.example.hiwin.teacher_version_bob.data.framework.pack.Content;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Package;

import java.util.Arrays;

public class LinePackage extends Package {


    public LinePackage(Content<?> content) {
        super(content.getRaw());
    }

    public LinePackage(Package pack) {
        super(pack.getEncoded());
    }

    public LinePackage(byte[] content) {
        super(content);
    }

    @Override
    public byte[] getEncoded() {
        if(content[content.length-1]=='\n'){
            return content;
        }else {
            byte[] newLineContent = Arrays.copyOf(content, content.length + 1);
            newLineContent[newLineContent.length - 1] = '\n';
            return newLineContent;
        }
    }

    @Override
    public byte[] getDecoded() {

        int indexOfEOL = -1;
        for (int i = 0; i < content.length; i++) {
            if (content[i] == '\n') {
                indexOfEOL = i;
                break;
            }
        }

        if (indexOfEOL == -1)
            indexOfEOL = content.length - 1;

        return Arrays.copyOf(content, indexOfEOL);
    }
}
