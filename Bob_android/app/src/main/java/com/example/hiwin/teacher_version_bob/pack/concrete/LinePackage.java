package com.example.hiwin.teacher_version_bob.pack.concrete;

import com.example.hiwin.teacher_version_bob.pack.framework.Package;

import java.util.Arrays;

public class LinePackage extends Package {

    public LinePackage(Package pack) {
        super(pack);
    }

    public LinePackage(byte[] bytes) {
        super(bytes);
    }


    @Override
    protected byte[] encode(byte[] encoded) {
        if(encoded[encoded.length-1]=='\n'){
            return encoded;
        }else {
            byte[] newLineContent = Arrays.copyOf(encoded, encoded.length + 1);
            newLineContent[newLineContent.length - 1] = '\n';
            return newLineContent;
        }
    }

    @Override
    protected byte[] decode(byte[] decoded) {
        int indexOfEOL = -1;
        for (int i = 0; i < decoded.length; i++) {
            if (decoded[i] == '\n') {
                indexOfEOL = i;
                break;
            }
        }

        if (indexOfEOL == -1)
            indexOfEOL = decoded.length - 1;

        return Arrays.copyOf(decoded, indexOfEOL);
    }
}
