package com.example.hiwin.teacher.BobJavaTester;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolSocket;
import com.example.hiwin.teacher.BobJavaTester.protocol.ServerProtocolSocket;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.fazecast.jSerialComm.SerialPort;

public class ServerProgram {
	ServerProtocolSocket socket;
	OutputStream os;

	public ServerProgram() throws IOException, InterruptedException {

		final SerialPort comPort = SerialPort.getCommPort("COM3");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		socket = new ServerProtocolSocket();
		os = comPort.getOutputStream();
		socket.attach(listener);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					byte[] buffer = new byte[1024];
					int len;
					
					while (true) {
						System.out.println("Reading");
						len = comPort.getInputStream().read(buffer);
						System.out.println("Received");
						byte[] data = Arrays.copyOf(buffer, len);

						ByteArrayInputStream bais = new ByteArrayInputStream(data);
						while (bais.available() > 0) {
							byte[] headerBytes = new byte[4];
							bais.read(headerBytes);
							PackageHeader header = null;
							print("raw:\n" + BytesInHexString(data) + "\n");
							try {
								header = new PackageHeader(headerBytes);
							} catch (IllegalArgumentException e) {
								System.err.println(e.getMessage());
								continue;
							}

							byte[] lackBytes = new byte[header.getlackBytesLength()];
							bais.read(lackBytes);

							print("header:\n" + BytesInHexString(headerBytes) + "\n");
							print("lackBytes:\n" + BytesInHexString(lackBytes) + "\n");
							socket.received(headerBytes, lackBytes);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

		Scanner scr = new Scanner(System.in);
		while (comPort.isOpen()) {

			synchronized (this) {
				if (socket.isConnected()) {
					System.out.println("Main Thread");
					scr.nextLine();
					String msg = "WwogIHsKICAibmFtZSI6ImFwcGxlIiwKICAibnVtYmVyIjozCiAgfSwKICB7CiAgICAibmFtZSI6InBlbiIsCiAgICAibnVtYmVyIjo0CiAgfSx7CiAgICAibmFtZSI6ImZ1Y2siLAogICAgIm51bWJlciI6NQogICAgCiAgfQpd";
					byte[] data = msg.getBytes(StandardCharsets.UTF_8);
					socket.writeBytes(data);
				}
			}
		}
		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new ServerProgram();
	}

	private ProtocolListener listener = new ProtocolListener() {

		public void OnProtocolDisconnected() {
			System.out.println("Disonnected");
		}

		public void OnProtocolConnected() {
			System.out.println("Connected");
		}

		public void OnReceiveDataPackage(byte[] data) {
			
		}

		public void OnWrite(byte[] data) {
			try {
				os.write(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
