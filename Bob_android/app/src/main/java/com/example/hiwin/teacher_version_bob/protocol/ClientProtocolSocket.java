package com.example.hiwin.teacher_version_bob.protocol;


import com.example.hiwin.teacher_version_bob.DebugUtil;
import com.example.hiwin.teacher_version_bob.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.PackageHeader;
import com.example.hiwin.teacher_version_bob.protocol.core.PackageType;
import com.example.hiwin.teacher_version_bob.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.data.SplitDataPackage;

public class ClientProtocolSocket extends ProtocolSocket {
    public ClientProtocolSocket() {

    }

    public void connect() {
        sendHello();
    }

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
            case ServerHello:
                ServerHelloPackage serverHelloPackage = new ServerHelloPackage(header, lack_bytes);

                status = serverHelloPackage.getStatusCode() == ServerHelloPackage.StatusCode.ALLOW
                        ? ConnecttionStatus.Connected
                        : ConnecttionStatus.Disconnected;
                if (pro_listener != null && status == ConnecttionStatus.Connected) {
                    pro_listener.OnProtocolConnected();
                } else if (pro_listener != null && status == ConnecttionStatus.Disconnected) {
                    pro_listener.OnProtocolDisconnected();
                }

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

    private void sendHello() {
        super.writePackage(new ClientHelloPackage());
    }

}