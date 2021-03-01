package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.util.ArrayList;

import com.example.hiwin.teacher.BobJavaTester.DebugUtil;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.Package;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public abstract class ProtocolSocket {
	public abstract void received(byte[] header_bytes,byte[] lack_bytes);
	
	static final int WRITE_DATA_RATE = 5000;

	public static enum ConnectionStatus {
		Connected, Disconnected
	}

	protected ConnectionStatus status;
	protected ProtocolListener pro_listener;
	protected DataPackage dataPackages;

	public void attach(ProtocolListener listener) {
		this.pro_listener = listener;
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
				if(pro_listener!=null)
					pro_listener.OnReceiveDataPackage(dataPackages.getData());
				dataPackages = null;
			}
		}

	}
	
	public void writeBytes(final byte[] data) {
		if (!isConnected())
			return;
		ArrayList<SplitDataPackage> dataPackages = DataPackage.splitPackage(data);
		for(SplitDataPackage _package:dataPackages) {
			writePackage(_package);
		}
	}
	
	protected void writePackage(Package _package) {
		DebugUtil.print("[Write Data]\n");
		DebugUtil.print(DebugUtil.BytesInHexString(_package.toBytes())+"\n");
		if (pro_listener != null) {
			pro_listener.OnWrite(_package.toBytes());
		}
	}
	
	public void close() {
		status=ConnectionStatus.Disconnected;
		if(pro_listener!=null) {
			pro_listener.OnProtocolDisconnected();
		}
	}
	

	public ConnectionStatus getStatus() {
		return status;
	}

	public synchronized boolean isConnected() {
		return status == ConnectionStatus.Connected;
	}
	
}
