package com.example.hiwin.teacher.BobJavaTester.protocol;

import com.example.hiwin.teacher.BobJavaTester.DebugUtil;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageType;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class ServerProtocolSocket extends ProtocolSocket {
	
	@Override
	public void received(byte[] header_bytes, byte[] lack_bytes) {
		PackageHeader header = new PackageHeader(header_bytes);
		PackageType type = PackageType.getPackageType(header);
		DebugUtil.print("\n[Receive data]\n");
		DebugUtil.print("Header:\n");
		DebugUtil.print(DebugUtil.BytesInHexString(header_bytes));
		DebugUtil.print("\n");

		DebugUtil.print("Data with cksum:\n");
		DebugUtil.print(DebugUtil.BytesInHexString(lack_bytes));
		DebugUtil.print("\n");
		DebugUtil.print("Type:");
		DebugUtil.print(type.toString());
		DebugUtil.print("\n<Receive data>");
		DebugUtil.print("\n\n\n");

		switch (type) {
		case ClientHello:
			ClientHelloPackage clientHelloPackage = new ClientHelloPackage(header, lack_bytes);
			onClientHelloReceive(clientHelloPackage);
			break;
		case SplitData:
			try {
				SplitDataPackage splitDataPackage = new SplitDataPackage(header, lack_bytes);
				onSplitDataReceive(splitDataPackage);
			} catch (IllegalArgumentException e) {
				DebugUtil.printE(e.getMessage());
			}
			break;
		default:

			break;
		}
	}

	protected void onSplitDataReceive(SplitDataPackage splitDataPackage) {
		if (splitDataPackage.getIndex() == 0) {
			dataPackages = new DataPackage(splitDataPackage.getTotal());
		}
		if (dataPackages != null) {
			if (dataPackages.receivedPackages() == 0 && splitDataPackage.getIndex() != 0)
				return;
			dataPackages.receive(splitDataPackage);
			if (dataPackages.isComplete()) {
				DebugUtil.print("Complete");
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
			status = ConnectionStatus.Connected;
		} else {
			serverHelloPackage = new ServerHelloPackage(ServerHelloPackage.StatusCode.DENY);
			status = ConnectionStatus.Disconnected;
		}

		writePackage(serverHelloPackage);
		if (pro_listener != null)
			pro_listener.OnProtocolConnected();
	}
}
