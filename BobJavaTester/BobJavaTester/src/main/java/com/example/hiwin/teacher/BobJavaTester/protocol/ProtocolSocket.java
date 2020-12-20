package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;

import com.example.hiwin.teacher.BobJavaTester.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.Package;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.VerifyResponsePackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.VerifyResponsePackage.Verify;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class ProtocolSocket implements Runnable, SerialListener {
	static final int WRITE_DATA_RATE = 100;

	public static enum ConnecttionStatus {
		Connected, Disconnected
	}

	private ConnecttionStatus status;
	private ProtocolListener pro_listener;
	private SerialListener ser_listener;
	private InputStream is;
	private DataPackage dataPackages;

	LinkedList<SplitDataPackage> tasks = null;

	public void connect(ProtocolListener listener) {
		this.pro_listener = listener;
		Executors.newSingleThreadExecutor().submit(this);
		print("start\n");
	}

	public void setSerialListener(SerialListener listener) {
		this.ser_listener = listener;
	}

	public void put(byte[] data) {
		print("Put data to inputstream.\n");

		synchronized (this) {
			this.is = new ByteArrayInputStream(data);
		}
	}

	public void run() {
		try {
			long send_timer = -1;
			int index = -1;
			while (true) {
				if (tasks != null && tasks.size() != 0) {

					if (System.currentTimeMillis() >= send_timer) {
						SplitDataPackage _package = tasks.getFirst();
						if (index != _package.getIndex()) {
							index = _package.getIndex();
							write(_package);
							send_timer = System.currentTimeMillis() + WRITE_DATA_RATE;
						}
					}

				}

				synchronized (this) {
					if (this.is == null)
						continue;

					byte[] headerBytes = new byte[4];
					is.read(headerBytes);
					PackageHeader header = new PackageHeader(headerBytes);
					byte[] lackBytes = new byte[header.getlackBytesLength()];
					is.read(lackBytes);
					onSerialRead(headerBytes, lackBytes);
					if (is.available() <= 0)
						is = null;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			status = ConnecttionStatus.Disconnected;
			onSerialIoError(e);
			try {
				is.close();
//				os.close();
			} catch (Exception ignored) {

			}

		}

	}

	private void onSplitDataReceive(SplitDataPackage splitDataPackage) {
		if (splitDataPackage.getIndex() == 0) {
			dataPackages = new DataPackage();
		}

		if (dataPackages != null) {
			if (dataPackages.size() == 0 && splitDataPackage.getIndex() != 0)
				return;
			dataPackages.add(splitDataPackage);

			if (dataPackages.isComplete()) {
				pro_listener.OnReceiveDataPackage(dataPackages.getData());
				dataPackages = null;
			}
		}
	}

	private void onClientHelloReceive(ClientHelloPackage clientHelloPackage) {
		if (isConnected())
			return;

		ServerHelloPackage serverHelloPackage = null;
		if (clientHelloPackage.verify()) {
			serverHelloPackage = new ServerHelloPackage(ServerHelloPackage.StatusCode.ALLOW);
			status = ConnecttionStatus.Connected;
		} else {
			serverHelloPackage = new ServerHelloPackage(ServerHelloPackage.StatusCode.DENY);
			status = ConnecttionStatus.Disconnected;
		}

		write(serverHelloPackage);
		if (pro_listener != null)
			pro_listener.OnProtocolConnected();
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
		case ServerHello:
			ServerHelloPackage serverHelloPackage = new ServerHelloPackage(header, lackBytes);

			status = serverHelloPackage.getStatusCode() == ServerHelloPackage.StatusCode.ALLOW
					? ConnecttionStatus.Connected
					: ConnecttionStatus.Disconnected;
			break;

		case ClientHello:
			ClientHelloPackage clientHelloPackage = new ClientHelloPackage(header, lackBytes);
			onClientHelloReceive(clientHelloPackage);
			break;
		case SplitData:
			try {
				SplitDataPackage splitDataPackage = new SplitDataPackage(header, lackBytes);

				VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(
						VerifyResponsePackage.Verify.OK);
				if (pro_listener != null) {
					pro_listener.OnWrite(verifyResponsePackage.toBytes());
				}
				onSplitDataReceive(splitDataPackage);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(
						VerifyResponsePackage.Verify.FAIL);
				if (pro_listener != null) {
					pro_listener.OnWrite(verifyResponsePackage.toBytes());
				}
			}

			break;
		case VerifyResponse:
			VerifyResponsePackage verifyResponsePackage=new VerifyResponsePackage(Verify.FAIL);
			try {
				verifyResponsePackage = new VerifyResponsePackage(header, lackBytes);
			}catch(IllegalArgumentException e) {
				printE(e.getMessage());
			}
			
			if (verifyResponsePackage.verify()) {
				synchronized (this) {
					if(!tasks.isEmpty())
						tasks.removeFirst();
				}
			}
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
		if (!isConnected())
			return;
		ArrayList<SplitDataPackage> datas = DataPackage.splitPackage(data);
		synchronized (this) {
			this.tasks = new LinkedList<SplitDataPackage>(datas);
		}
	}

	public synchronized void write(Package _package) {
		print("[Write Data]\n");
		print(BytesInHexString(_package.toBytes()));
		print("\n");
		if (pro_listener != null) {
			pro_listener.OnWrite(_package.toBytes());
		}
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

	public ConnecttionStatus getStatus() {
		return status;
	}

	public synchronized boolean isConnected() {
		return status == ConnecttionStatus.Connected;
	}

	public static void print(String str) {
		System.out.print(str);
	}
	
	public static void printE(String str) {
		System.err.print(str);
	}
}
