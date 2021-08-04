package com.example.hiwin.teacher_version_bob.communication;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SerialDataListener implements SerialListener {
    private ConnectStatus status = ConnectStatus.Disconnected;

    protected abstract void onStringDataReceived(String content);

    protected abstract void onConnected();
    protected abstract void onIOError(Exception e);
    protected abstract void onConnectionError(Exception e);
    public abstract void disconnect();
    protected abstract void onDisconnected();
    protected abstract void onBase64DecodeError(IllegalArgumentException e);

    private final Queue<Byte> buffer = new LinkedList<>();
    private long delay_timer;

    private void onBytesDataReceive(byte[] bytes) {
        try {
            String content = new String(Base64.decode(bytes, Base64.DEFAULT), StandardCharsets.UTF_8);
            onStringDataReceived(content);
        } catch (IllegalArgumentException e) {
            onBase64DecodeError(e);
        }
    }



    @Override
    public final void onSerialConnect() {
        status = ConnectStatus.Connected;
        onConnected();
    }



    @Override
    public final void onSerialConnectError(Exception e) {
        status = ConnectStatus.Disconnected;
        disconnect();
        onDisconnected();
        onConnectionError(e);
    }



    @Override
    public final void onSerialRead(byte[] data) {
        long current_time = System.currentTimeMillis();

        if (current_time >= delay_timer) {
            buffer.clear();
        }

        int indexOfEOL = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == '\n')
                indexOfEOL = i;
        }

        if (indexOfEOL == -1) {
            for (byte datum : data) {
                buffer.offer(datum);
            }
            delay_timer = current_time + 1500;

        } else {
            for (int i = 0; i < indexOfEOL; i++) {
                buffer.offer(data[i]);
            }

            byte[] bytes = new byte[buffer.size()];
            for (int i = 0; i < bytes.length; i++) {
                Byte b = buffer.poll();
                if (b == null)
                    throw new RuntimeException();
                bytes[i] = b;
            }
            onBytesDataReceive(bytes);
        }

    }


    @Override
    public final void onSerialIoError(Exception e) {
        status = ConnectStatus.Disconnected;
        disconnect();
        onDisconnected();
        onIOError(e);
    }

    public ConnectStatus getConnectStatus() {
        return status;
    }

}
