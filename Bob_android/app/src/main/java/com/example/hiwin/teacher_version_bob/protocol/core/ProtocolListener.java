package com.example.hiwin.teacher_version_bob.protocol.core;

public interface ProtocolListener {
    public void OnProtocolConnected();
    public void OnProtocolDisconnected();
    public void OnReceiveDataPackage(byte[] data);
    public void OnWrite(byte[] data);
    
}
