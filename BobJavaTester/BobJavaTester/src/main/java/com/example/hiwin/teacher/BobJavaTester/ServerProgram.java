package com.example.hiwin.teacher.BobJavaTester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;


import com.example.hiwin.teacher.BobJavaTester.Receiver.ReceiveListener;
import com.fazecast.jSerialComm.SerialPort;

public class ServerProgram implements ReceiveListener{
	OutputStream os;

	public ServerProgram() throws IOException, InterruptedException {

		final SerialPort comPort = SerialPort.getCommPort("COM5");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		os = comPort.getOutputStream();
		
		Thread th=new Thread(new Receiver(comPort.getInputStream(),this));
		th.start();
		
		Scanner scr = new Scanner(System.in);
		while (comPort.isOpen()) {
				System.out.println("Main Thread");
				scr.nextLine();
				FileInputStream fis=new FileInputStream(new File("sample.json"));
				BufferedReader reader=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				StringBuilder sb=new StringBuilder();
				
				String buf=reader.readLine();
				sb.append(buf);
				buf=reader.readLine();
				while(buf!=null) {
					sb.append("\n");
					sb.append(buf);
					buf=reader.readLine();
				}
				
				byte[] raw_bytes=sb.toString().getBytes(StandardCharsets.UTF_8);
				String msg=Base64.getUrlEncoder().encodeToString(raw_bytes)+"\n";
				byte[] data = msg.getBytes(StandardCharsets.UTF_8);
				os.write(data);
				System.out.println(msg);
				System.out.println(BytesInHexString(data));
				System.out.println(data.length);
			
		}
		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new ServerProgram();
	}

	String BytesInHexString(byte[] raw) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		for (byte b : raw) {
			sb.append("0x").append(Integer.toHexString(b & 0xFF)).append(",");
		}
		sb.append("}");
		return sb.toString();
	}

	public static void print(String str) {
		System.out.print(str);
	}

	public void onReveive(String str) {
		System.out.println("<<<receive>>>");
		System.out.println(str);
		System.out.println("<<<receive>>>");
		
	}
}
