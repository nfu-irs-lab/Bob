package com.example.hiwin.teacher_version_bob.protocol;


public interface ProtocolListener {
	
    public void OnProtocolConnected();
    public void OnProtocolDisconnected();
    public void OnConnectionRejected(ServerHelloPackage.StatusCode statusCode);
    public void OnReceiveMessage(String message);
}
