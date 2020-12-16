package com.example.hiwin.teacher_version_bob.protocol;

import android.util.Base64;
import android.util.Log;

import com.example.hiwin.teacher_version_bob.communication.SerialService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientProtocol {
    private ProtocolListener listener;
    private DataPackage dataPackages;
    public ClientProtocol() {
        dataPackages=new DataPackage();
    }

    public void attach(ProtocolListener listener) {
        this.listener=listener;
    }

    public void receive(byte[] newData,SerialService service) {

        Package.Type type = Package.Type.getPackageType(newData);
        switch (type) {
            case ServerHello:
                OnReceiveServerHello(newData);
                break;
            case SplitData:
                onSplitDataReceive(newData);
            default:

                break;
        }
    }

    private void onSplitDataReceive(byte[] newData) {
        SplitDataPackage splitDataPackage=new SplitDataPackage(newData);
        if(dataPackages.size()==0&&splitDataPackage.getIndex()!=0)return;

        if(dataPackages!=null){
            dataPackages.add(splitDataPackage);

            if(dataPackages.isComplete()){
                Log.d("ProtocolLog","Receive Complete");
                StringBuffer sb=new StringBuffer();
                sb.append("[");
                for(byte b:dataPackages.getData()){
                    sb.append("0x");
                    sb.append(Integer.toHexString(b));
                    sb.append(",");
                }
                sb.append("]");
                Log.d("ProtocolLog",sb.toString());
                listener.OnReceiveData(dataPackages.getData());
                dataPackages.clear();
            }
        }
    }

    private void OnReceiveServerHello(byte[] data) {
        ServerHelloPackage sh=new ServerHelloPackage(data);
        ServerHelloPackage.StatusCode statusCode=sh.getStatusCode();
        Log.d("ProtocolLog",statusCode.toString());
        if(statusCode== ServerHelloPackage.StatusCode.ALLOW){
            listener.OnProtocolConnected();
        }else{
            listener.OnConnectionRejected(statusCode);
        }

    }

    public void connect(SerialService service){
        ClientHelloPackage clientHelloPackage = new ClientHelloPackage();
        byte[] data=clientHelloPackage.toBytes();
        try {
            service.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
