package com.example.hiwin.teacher.BobJavaTester.protocol.core;

public interface ProtocolListener {
    public void OnProtocolConnected();
    public void OnProtocolDisconnected();
    public void OnReceiveDataPackage(byte[] data);
    
}
