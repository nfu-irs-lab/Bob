package com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete;

import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialReadStrategy;

import java.util.LinkedList;
import java.util.Queue;

public class ReadLineStrategy implements SerialReadStrategy {

    private final Queue<Byte> buffer = new LinkedList<>();
    private long delay_timer;
    private boolean isIntegralPackage;
    private byte[] package_data;

    @Override
    public void warp(byte[] data) {
        long current_time = System.currentTimeMillis();

        if (current_time >= delay_timer) {
            buffer.clear();
        }

        int indexOfEOL = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == '\n') {
                indexOfEOL = i;
                break;
            }
        }

        isIntegralPackage = indexOfEOL != -1;

        if (!isIntegralPackage) {
            package_data = null;
            for (byte datum : data) {
                buffer.offer(datum);
            }
            delay_timer = current_time + 1500;

        } else {
            for (int i = 0; i < indexOfEOL; i++) {
                buffer.offer(data[i]);
            }

            package_data = new byte[buffer.size()];
            for (int i = 0; i < package_data.length; i++) {
                Byte b = buffer.poll();
                if (b == null)
                    throw new RuntimeException();
                package_data[i] = b;
            }

            if (indexOfEOL + 1 < data.length) {
                for (int i = indexOfEOL + 1; i < data.length; i++) {
                    buffer.offer(data[i]);
                }
                delay_timer = current_time + 1500;
            }
        }
    }

    @Override
    public boolean isIntegralPackage() {
        return isIntegralPackage;
    }

    @Override
    public byte[] getPackage() {
        if (isIntegralPackage) {
            return package_data;
        } else {
            return null;
        }
    }
}
