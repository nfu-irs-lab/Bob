package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class ProtocolSocket implements Runnable, SerialListener {
	private DataPackage dataPackages;

	public static enum ConnecttionStatus {
		Connected, Disconnected
	}

	private ConnecttionStatus status;
	private ProtocolListener pro_listener;
	private SerialListener ser_listener;
	private final InputStream is;
	private final OutputStream os;

	public ProtocolSocket(InputStream is, OutputStream os) {
		this.os = os;
		this.is = is;
	}

	public void connect(ProtocolListener listener) {
		this.pro_listener = listener;
		Executors.newSingleThreadExecutor().submit(this);
	}

	public void setSerialListener(SerialListener listener) {
		this.ser_listener = listener;
	}

	public void run() {
		try {

			// noinspection InfiniteLoopStatement
			while (true) {
				status = ConnecttionStatus.Connected;
				byte[] headerBytes = new byte[4];
				is.read(headerBytes);
				PackageHeader header = new PackageHeader(headerBytes);
				byte[] lackBytes = new byte[header.getlackBytesLength()];
				is.read(lackBytes);
				onSerialRead(headerBytes, lackBytes);

			}
		} catch (Exception e) {
			e.printStackTrace();
			status = ConnecttionStatus.Disconnected;
			onSerialIoError(e);
			try {
				is.close();
				os.close();
			} catch (Exception ignored) {

			}

		}

	}

	private void onSplitDataReceive(SplitDataPackage splitDataPackage) {
		if (dataPackages.size() == 0 && splitDataPackage.getIndex() != 0)
			return;

		if (dataPackages != null) {
			dataPackages.add(splitDataPackage);

			if (dataPackages.isComplete()) {
				pro_listener.OnReceiveDataPackage(dataPackages.getData());
				dataPackages.clear();
			}
		}
	}

	private void onClientHelloReceive(ClientHelloPackage clientHelloPackage) {
		ServerHelloPackage serverHelloPackage = null;
		if (clientHelloPackage.verify()) {
			serverHelloPackage = new ServerHelloPackage(ServerHelloPackage.StatusCode.ALLOW);
		} else {
			serverHelloPackage = new ServerHelloPackage(ServerHelloPackage.StatusCode.DENY);
		}

		try {
			write(serverHelloPackage);
			if (pro_listener != null)
				pro_listener.OnProtocolConnected();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onSerialRead(byte[] headerBytes, byte[] lackBytes) {
		PackageHeader header = new PackageHeader(headerBytes);
		Package.Type type = Package.Type.getPackageType(header);
		print("[Receive data]\n");
		print("Header:\n");
		print(BytesInHexString(headerBytes));
		print("\n");

		print("Data with cksum:\n");
		print(BytesInHexString(lackBytes));
		print("\n");
		print("Type:");
		print(type.toString());
		print("\n<Receive data>");
		print("\n\n\n");

		switch (type) {
		case ClientHello:
			ClientHelloPackage clientHelloPackage = new ClientHelloPackage(header, lackBytes);
			onClientHelloReceive(clientHelloPackage);
			break;
		case SplitData:
			SplitDataPackage splitDataPackage = new SplitDataPackage(header, lackBytes);
			onSplitDataReceive(splitDataPackage);
			break;
		default:

			break;
		}

		if (ser_listener != null)
			ser_listener.onSerialRead(headerBytes, lackBytes);

	}

	public void onSerialConnect() {
		// TODO Auto-generated method stub

	}

	public void onSerialConnectError(Exception e) {
		// TODO Auto-generated method stub

	}

	public void onSerialIoError(Exception e) {

	}

	public void write(byte[] data) throws InterruptedException, IOException {
		ArrayList<SplitDataPackage> datas = DataPackage.splitPackage(data);
		for (int i = 0; i < datas.size(); i++) {
			write(datas.get(i));
			Thread.sleep(1);
		}
	}

	private void write(Package _package) throws IOException {
		print("[Write Data]\n");
		print(BytesInHexString(_package.toBytes()));
		print("\n");
		os.write(_package.toBytes());
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

}
