package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.util.ArrayList;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.Package;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class ProtocolSocket {
	static final int WRITE_DATA_RATE = 5000;

	public static enum ConnecttionStatus {
		Connected, Disconnected
	}

	private ConnecttionStatus status;
	private ProtocolListener pro_listener;
	private DataPackage dataPackages;

	private int index = 0;

	public void connect(ProtocolListener listener) {
		this.pro_listener = listener;
	}

	public void received(byte[] header_bytes,byte[] lack_bytes) {
		onSerialRead(header_bytes,lack_bytes);
	}

	private void onSplitDataReceive(SplitDataPackage splitDataPackage) {
		if (splitDataPackage.getIndex() == 0) {
			dataPackages = new DataPackage(splitDataPackage.getTotal());
		}
		if (dataPackages != null) {
			if (dataPackages.receivedPackages() == 0 && splitDataPackage.getIndex() != 0)
				return;
			dataPackages.receive(splitDataPackage);
			if (dataPackages.isComplete()) {
				print("Complete");
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
		print("\n[Receive data]\n");
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
				onSplitDataReceive(splitDataPackage);
			} catch (IllegalArgumentException e) {
				printE(e.getMessage());
			}
			
			
//			try {
//				SplitDataPackage splitDataPackage = new SplitDataPackage(header, lackBytes);
//
//				VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(
//						VerifyResponsePackage.Verify.OK);
//				if (pro_listener != null) {
//					pro_listener.OnWrite(verifyResponsePackage.toBytes());
//				}
//				onSplitDataReceive(splitDataPackage);
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//				VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(
//						VerifyResponsePackage.Verify.FAIL);
//				if (pro_listener != null) {
//					pro_listener.OnWrite(verifyResponsePackage.toBytes());
//				}
//			}

			break;
//		case VerifyResponse:
//			VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(Verify.FAIL);
//			try {
//				verifyResponsePackage = new VerifyResponsePackage(header, lackBytes);
//			} catch (IllegalArgumentException e) {
//				printE(e.getMessage());
//			}
//
//			if (verifyResponsePackage.verify()) {
//				synchronized (this) {
//					if (tasks != null && !tasks.isEmpty()) {
//						HashMap<String, Object> map = tasks.get(index);
//						map.put("verify", Verify.OK);
//						SplitDataPackage pack = (SplitDataPackage) map.get("package");
//						tasks.set(index, map);
//						if (index < pack.getTotal() - 1)
//							index++;
//					}
//
//				}
//			}
//			break;
		default:

			break;
		}
	}

	public void write(final byte[] data) {
		if (!isConnected())
			return;
		ArrayList<SplitDataPackage> dataPackages = DataPackage.splitPackage(data);
		for(SplitDataPackage _package:dataPackages) {
			write(_package);
		}
	}
	
//	public void write(byte[] data) throws InterruptedException, IOException {
//		if (!isConnected())
//			return;
//		ArrayList<SplitDataPackage> datas = DataPackage.splitPackage(data);
//		synchronized (this) {
//			this.tasks = new ArrayList<HashMap<String, Object>>();
//			for (int i = 0; i < datas.size(); i++) {
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				map.put("verify", Verify.FAIL);
//				map.put("package", datas.get(i));
//				this.tasks.add(map);
//			}
//		}
//	}

	public void write(Package _package) {
		print("[Write Data]\n");
		print(BytesInHexString(_package.toBytes())+"\n");
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
