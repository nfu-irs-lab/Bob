package com.example.hiwin.teacher.BobJavaTester;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.Base64.Encoder;

import com.example.hiwin.teacher.BobJavaTester.protocol.ClientHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolSocket;
import com.example.hiwin.teacher.BobJavaTester.protocol.ServerProtocol;
import com.example.hiwin.teacher.BobJavaTester.protocol.SplitDataPackage;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MainProgram2 {
	ProtocolSocket socket;

	public MainProgram2() throws IOException, InterruptedException {

		SerialPort comPort = SerialPort.getCommPort("COM3");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		socket=new ProtocolSocket(comPort.getInputStream(),comPort.getOutputStream());
		socket.connect(listener);
		
		Scanner scr=new Scanner(System.in);
		Thread.sleep(5000);
		
		while (comPort.isOpen()) {
//			Object obj=socket.read();
//			if (obj instanceof ClientHelloPackage) {
//				System.out.println("Hello");
//			}
			
//			byte[] data= {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
			
			String msg=scr.nextLine();
			byte[] data=msg.getBytes(StandardCharsets.UTF_8);
			
			socket.write(data);
			Thread.sleep(50);
		}
		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new MainProgram2();
	}


	private ProtocolListener listener = new ProtocolListener() {

		public void OnProtocolDisconnected() {
			System.out.println("Cisonnected");
		}

		public void OnProtocolConnected() {
			System.out.println("Connected");
		}
		
		public void OnReceiveDataPackage(byte[] data){
			
		}
		
	};
	
}
