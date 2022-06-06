package com.example.hiwin.teacher_version_bob.communication;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PackageCodecFacade {
    private PackageCodecFacade() {

    }

    public static byte[] encode(byte[] bytes) {
        byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
        byte[] package_ = Arrays.copyOf(encoded, encoded.length + 1);
        package_[package_.length - 1] = '\n';

        return package_;

    }

    public static byte[] encodeString(String msg) {
        return encode(msg.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(byte[] bytes, boolean hasEOL) {
        if (hasEOL) {
            return Base64.decode(Arrays.copyOfRange(bytes, 0, bytes.length), Base64.DEFAULT);
        } else {
            return Base64.decode(bytes, Base64.DEFAULT);
        }
    }

    public static String decodeString(byte[] bytes, boolean hasEOL) {
        if (hasEOL) {
            return new String(Base64.decode(Arrays.copyOfRange(bytes, 0, bytes.length), Base64.DEFAULT), StandardCharsets.UTF_8);
        } else {
            return new String(Base64.decode(bytes, Base64.DEFAULT), StandardCharsets.UTF_8);
        }
    }

}
