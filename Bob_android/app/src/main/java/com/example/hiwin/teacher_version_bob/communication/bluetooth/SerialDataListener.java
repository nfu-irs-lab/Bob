package com.example.hiwin.teacher_version_bob.communication.bluetooth;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public abstract class SerialDataListener implements SerialListener {
    private ConnectStatus status = ConnectStatus.Disconnected;

    protected abstract void onStringDataReceived(String content);

    protected abstract void onConnected();
    protected abstract void onIOError(Exception e);
    protected abstract void onConnectionError(Exception e);
    public abstract void disconnect();
    protected abstract void onDisconnected();
    protected abstract void onBase64DecodeError(IllegalArgumentException e);

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
        onBytesDataReceive(data);
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
