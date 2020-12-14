import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import protocol.ServerProtocol;
import protocol.MessagePackage;
import protocol.ProtocolListener;

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
			protocol.sendMsg("Hello World 你好世界");
			Thread.sleep(10000);
		}
		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new MainProgram();
	}

	@Override
	public int getListeningEvents() {
		// TODO Auto-generated method stub
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
//		SerialPort comPort = event.getSerialPort();
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
		@Override
		public void OnProtocolDisconnected() {
			System.out.println("Cisonnected");
		}

		@Override
		public void OnProtocolConnected() {
			System.out.println("Connected");
		}
	};
}
