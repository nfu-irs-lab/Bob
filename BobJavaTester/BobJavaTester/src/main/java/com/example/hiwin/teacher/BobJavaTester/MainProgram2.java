package com.example.hiwin.teacher.BobJavaTester;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolSocket;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.fazecast.jSerialComm.SerialPort;

public class MainProgram2 {
	ProtocolSocket socket;

	public MainProgram2() throws IOException, InterruptedException {

		final SerialPort comPort = SerialPort.getCommPort("COM3");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		socket = new ProtocolSocket(null,comPort.getOutputStream());
		socket.connect(listener);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					byte[] buffer = new byte[1024];
					int len;
					// noinspection InfiniteLoopStatement
					while (true) {
						System.out.println("Reading");
						len = comPort.getInputStream().read(buffer);
						System.out.println("Received");
						byte[] data = Arrays.copyOf(buffer, len);
						print(BytesInHexString(data)+"\n");
						socket.put(data);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

		Scanner scr = new Scanner(System.in);
		while (comPort.isOpen()) {
//			String msg = scr.nextLine();
			if(socket.isConnected()) {
				System.out.println("Main Thread");
				String msg="WwogIHsKICAibmFtZSI6ImFwcGxlIiwKICAibnVtYmVyIjozCiAgfSwKICB7CiAgICAibmFtZSI6InBlbiIsCiAgICAibnVtYmVyIjo0CiAgfSx7CiAgICAibmFtZSI6ImZ1Y2siLAogICAgIm51bWJlciI6NQogICAgCiAgfQpd";
				
				byte[] data = msg.getBytes(StandardCharsets.UTF_8);

				socket.write(data);
				Thread.sleep(5000);
			}
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

		public void OnReceiveDataPackage(byte[] data) {

		}

	};
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
}
