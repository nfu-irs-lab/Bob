package com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete;

import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialReadStrategy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ReadLineStrategy implements SerialReadStrategy {

    private final List<Byte> buffer = new LinkedList<>();
    private List<byte[]> packages = new LinkedList<>();

    @Override
    public void warp(byte[] data) {

        for (byte b : data) {
            buffer.add(b);
        }

        int indexOfEOL = getIndexOfFirstEOL(buffer);
        while (indexOfEOL != -1) {
            packages.add(subArray(buffer, 0, indexOfEOL));

            for (int i = 0; i <= indexOfEOL; i++) {
                buffer.remove(0);
            }

            indexOfEOL = getIndexOfFirstEOL(buffer);
        }
    }

    @Override
    public boolean hasNextPackage() {
        return packages.size() != 0;
    }

    private int getIndexOfFirstEOL(List<Byte> data) {
        int indexOfEOL = -1;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == '\n') {
                indexOfEOL = i;
                break;
            }
        }
        return indexOfEOL;
    }

    private byte[] subArray(List<Byte> data, int start, int len) {
        byte[] buf = new byte[len];
        for (int i = start; i < start + len; i++) {
            buf[i - start] = data.get(i);
        }
        return buf;
    }

    @Override
    public byte[] nextPackage() {
        if (hasNextPackage()) {
            byte[] data = packages.get(0);
            byte[] buf = Arrays.copyOf(data, data.length);
            packages.remove(0);
            return buf;
        } else {
            throw new RuntimeException("No package");
        }
    }
}
