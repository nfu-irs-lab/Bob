package com.example.hiwin.teacher_version_bob.protocol;

public interface SerialListener {
    void onSerialConnect();
    void onSerialConnectError(Exception e);
    void onSerialRead(byte[] header, byte[] lackBytes);
    void onSerialIoError(Exception e);
}
