package protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;

import protocol.ServerHelloPackage.StatusCode;

public class ServerProtocol{
	private ProtocolListener listener;
	
	public ServerProtocol() {
		
	}
	
	public void attach(ProtocolListener listener) {
		this.listener=listener;
	}
	
	public void receive(SerialPort comPort) {
		System.out.println("Available: " + comPort.bytesAvailable() + " bytes.");
		byte[] newData = new byte[comPort.bytesAvailable()];
		InputStream in = comPort.getInputStream();
		OutputStream out = comPort.getOutputStream();
		try {
			in.read(newData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dumpBytes(newData);
		
		Package.Type type = Package.Type.getPackageType(newData);
		switch (type) {
		case ClientHello:
			OnReceiveClientHello(newData,out);
			break;
		case ClientBye:
			listener.OnProtocolDisconnected();
			break;
	
		default:

			break;
		}
	}
	
	void dumpBytes(byte[] data) {
		System.out.print("[");
		for (byte b : data) {
			System.out.print(b + " ");
		}
		System.out.print("]\n");
	}
	
	private void OnReceiveClientHello(byte[] data,OutputStream out) {
		ServerHelloPackage sh;
		ClientHelloPackage ch = new ClientHelloPackage(data);
		if (ch.verify()) {
			System.out.println("Client Hello");
			sh = new ServerHelloPackage(StatusCode.ALLOW);
			listener.OnProtocolConnected();
		} else {
			sh = new ServerHelloPackage(StatusCode.NOT_SUPPORT);
//			listener.OnProtocolDisconnected(StatusCode.NOT_SUPPORT);
		}
		
		try {
			out.write(sh.toBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
