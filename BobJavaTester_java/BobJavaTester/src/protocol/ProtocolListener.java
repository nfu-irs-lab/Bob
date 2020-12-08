package protocol;

import protocol.ServerHelloPackage.StatusCode;

public interface ProtocolListener {
	
    public void OnProtocolConnected();
    public void OnProtocolDisconnected();
}
