package com.example.hiwin.teacher_version_bob.communication.bluetooth.framework;

public interface SerialReadStrategy {
    void warp(byte[] data);
    boolean hasNextPackage();
    byte[] nextPackage();

}
