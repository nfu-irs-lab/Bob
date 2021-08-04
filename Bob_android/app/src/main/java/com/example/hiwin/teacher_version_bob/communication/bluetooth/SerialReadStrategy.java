package com.example.hiwin.teacher_version_bob.communication.bluetooth;

public interface SerialReadStrategy {
    void warp(byte[] data);
    boolean isIntegralPackage();
    byte[] getPackage();

}
