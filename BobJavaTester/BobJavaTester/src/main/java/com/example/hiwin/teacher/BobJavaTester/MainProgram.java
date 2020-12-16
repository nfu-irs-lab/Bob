package com.example.hiwin.teacher.BobJavaTester;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.Base64.Encoder;

import com.example.hiwin.teacher.BobJavaTester.protocol.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.ServerProtocol;
import com.example.hiwin.teacher.BobJavaTester.protocol.SplitDataPackage;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MainProgram implements SerialPortDataListener {
	ServerProtocol protocol;

	public MainProgram() throws IOException, InterruptedException {

		SerialPort comPort = SerialPort.getCommPort("COM3");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.addDataListener(this);
		protocol = new ServerProtocol(comPort.getInputStream(), comPort.getOutputStream());
		protocol.attach(listener);

		Scanner scr=new Scanner(System.in);
		Thread.sleep(5000);
		while (comPort.isOpen()) {
//			byte[] data= {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
			
			String msg=scr.nextLine();
			byte[] data=msg.getBytes(StandardCharsets.UTF_8);
			ArrayList<SplitDataPackage> datas=DataPackage.splitPackage(data);
			for(int i=0;i<datas.size();i++) {
				System.out.println(i+":");
				dumpBytes(datas.get(i).toBytes());
				protocol.sendBytes(datas.get(i).toBytes());
				Thread.sleep(50);
			}
			
//			protocol.sendMsg("[{\"name\":\"apple\",\"number\":3},{\"name\":\"pen\",\"number\":4 }]");
		}
		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new MainProgram();
	}

	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	public void serialEvent(SerialPortEvent event) {
		protocol.receive();
	}

	void dumpBytes(byte[] data) {
		System.out.print("[");
		for (byte b : data) {
			System.out.print(b + " ");
		}
		System.out.print("]\n");
	}

	private ProtocolListener listener = new ProtocolListener() {

		public void OnProtocolDisconnected() {
			System.out.println("Cisonnected");
		}

		public void OnProtocolConnected() {
			System.out.println("Connected");
		}
	};
	
	private byte[] encodeMessage(String msg) {
		Encoder encoder = Base64.getEncoder();
		String str = null;
//		try {
		str = encoder.encodeToString(msg.getBytes(StandardCharsets.UTF_8));
//			System.out.println(str);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
		return str.getBytes(StandardCharsets.UTF_8);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
	}
}
