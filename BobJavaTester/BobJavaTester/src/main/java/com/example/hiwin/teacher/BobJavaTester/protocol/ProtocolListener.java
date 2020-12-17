package com.example.hiwin.teacher.BobJavaTester.protocol;

public interface ProtocolListener {
    public void OnProtocolConnected();
    public void OnProtocolDisconnected();
    public void OnReceiveDataPackage(byte[] data);
    
}
