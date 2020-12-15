package com.example.hiwin.teacher.BobJavaTester;

import java.io.IOException;

import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.ServerProtocol;
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
		while (comPort.isOpen()) {
			protocol.sendMsg("[{\"name\":\"apple\",\"number\":3},{\"name\":\"pen\",\"number\":4 }]");
			Thread.sleep(10000);
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
}
