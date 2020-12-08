import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import protocol.ServerProtocol;
import protocol.ProtocolListener;

public class Tester implements SerialPortDataListener {
	ServerProtocol protocol;
	public Tester() throws IOException, InterruptedException {
		protocol=new ServerProtocol();
		protocol.attach(new ProtocolListener() {
			
			@Override
			public void OnProtocolDisconnected() {
				System.out.println("Cisonnected");
			}
			
			@Override
			public void OnProtocolConnected() {
				System.out.println("Connected");
			}
		});
		
		SerialPort comPort = SerialPort.getCommPort("COM3");
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.addDataListener(this);

		while (comPort.isOpen()) {
			Thread.sleep(10);
		}

		comPort.closePort();
	}

	public static void main(String[] args) throws Exception {
		new Tester();
	}

	@Override
	public int getListeningEvents() {
		// TODO Auto-generated method stub
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		SerialPort comPort = event.getSerialPort();
		protocol.receive(comPort);
	}

	void dumpBytes(byte[] data) {
		System.out.print("[");
		for (byte b : data) {
			System.out.print(b + " ");
		}
		System.out.print("]\n");
	}
}
