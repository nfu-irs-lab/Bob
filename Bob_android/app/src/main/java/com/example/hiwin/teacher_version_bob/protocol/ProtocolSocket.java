package com.example.hiwin.teacher_version_bob.protocol;

import android.util.Log;

import com.example.hiwin.teacher_version_bob.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.PackageHeader;
import com.example.hiwin.teacher_version_bob.protocol.core.ProtocolListener;
import com.example.hiwin.teacher_version_bob.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.VerifyResponsePackage;
import com.example.hiwin.teacher_version_bob.protocol.core.data.DataPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.data.SplitDataPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.Package;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executors;


public class ProtocolSocket implements Runnable, SerialListener {
    static final int WRITE_DATA_RATE = 5000;

    public static enum ConnecttionStatus {
        Connected, Disconnected
    }

    private ConnecttionStatus status;
    private ProtocolListener pro_listener;
    private SerialListener ser_listener;
    private InputStream is;
    private DataPackage dataPackages;

//	LinkedList<SplitDataPackage> tasks = null;

    ArrayList<HashMap<String, Object>> tasks = null;

    private int index = 0;

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
            while (true) {
                if (tasks != null && tasks.size() != 0) {

                    if (System.currentTimeMillis() >= send_timer) {
                        HashMap<String, Object> map = tasks.get(index);
                        SplitDataPackage _package = (SplitDataPackage) map.get("package");
                        VerifyResponsePackage.Verify verify = (VerifyResponsePackage.Verify) map.get("verify");
                        if (verify == VerifyResponsePackage.Verify.FAIL) {
                            write(_package);
                            send_timer = System.currentTimeMillis() + WRITE_DATA_RATE;
                        }

//						if (index != _package.getIndex()) {
//							index = _package.getIndex();
//							write(_package);
//							send_timer = System.currentTimeMillis() + WRITE_DATA_RATE;
//						}
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
            printE(e.getMessage());
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
            dataPackages = new DataPackage(splitDataPackage.getTotal());
        }
        if (dataPackages != null) {
            if (dataPackages.receivedPackages() == 0 && splitDataPackage.getIndex() != 0)
                return;
            dataPackages.receive(splitDataPackage);
            if (dataPackages.isComplete()) {
                pro_listener.OnReceiveDataPackage(dataPackages.getData());
                dataPackages = null;
            }
        }
//		if (dataPackages != null) {
//			if (dataPackages.size() == 0 && splitDataPackage.getIndex() != 0)
//				return;
//			dataPackages.add(splitDataPackage);
//
//			if (dataPackages.isComplete()) {
//				pro_listener.OnReceiveDataPackage(dataPackages.getData());
//				dataPackages = null;
//			}
//		}
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

        PackageHeader header=null;
        try{
            header = new PackageHeader(headerBytes);
        }catch (IllegalArgumentException e){
            printE(e.getMessage());
        }
        if(header==null)return;
//        PackageHeader header = new PackageHeader(headerBytes);
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
                VerifyResponsePackage verifyResponsePackage = new VerifyResponsePackage(VerifyResponsePackage.Verify.FAIL);
                try {
                    verifyResponsePackage = new VerifyResponsePackage(header, lackBytes);
                } catch (IllegalArgumentException e) {
                    printE(e.getMessage());
                }

                if (verifyResponsePackage.verify()) {
                    synchronized (this) {
                        if (tasks != null && !tasks.isEmpty()) {
                            HashMap<String, Object> map = tasks.get(index);
                            map.put("verify", VerifyResponsePackage.Verify.OK);
                            SplitDataPackage pack = (SplitDataPackage) map.get("package");
                            tasks.set(index, map);
                            if (index < pack.getTotal() - 1)
                                index++;
                        }

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
            this.tasks = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < datas.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("verify", VerifyResponsePackage.Verify.FAIL);
                map.put("package", datas.get(i));
                this.tasks.add(map);
            }

//			this.tasks = new LinkedList<SplitDataPackage>(datas);
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
        Log.d("ProtocolLog",str);
}

    public static void printE(String str) {
        Log.e("ProtocolLog",str);
    }

}