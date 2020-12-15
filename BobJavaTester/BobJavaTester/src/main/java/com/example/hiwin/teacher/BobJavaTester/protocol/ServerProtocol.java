package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.hiwin.teacher.BobJavaTester.protocol.ServerHelloPackage.StatusCode;



public class ServerProtocol{
	private ProtocolListener listener;
	private final InputStream is;
	private final OutputStream os;
	
	public ServerProtocol(InputStream is,OutputStream os) {
		this.os=os;
		this.is=is;
	}
	
	
	public void attach(ProtocolListener listener) {
		this.listener=listener;
	}
	public void sendMsg(String msg) throws IOException {
		MessagePackage data=new MessagePackage(msg);
		os.write(data.toBytes());
	}
	public void receive() {
		byte[] newData = null;
		try {
			newData = new byte[is.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			is.read(newData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dumpBytes(newData);
		
		Package.Type type = Package.Type.getPackageType(newData);
		switch (type) {
		case ClientHello:
			OnReceiveClientHello(newData,os);
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
