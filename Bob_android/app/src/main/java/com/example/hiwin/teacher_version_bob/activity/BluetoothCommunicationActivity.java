package com.example.hiwin.teacher_version_bob.activity;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.ReadLineStrategy;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.SerialSocket;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.service.SerialService;
import com.example.hiwin.teacher_version_bob.pack.concrete.Base64Package;
import com.example.hiwin.teacher_version_bob.pack.concrete.LinePackage;
import com.example.hiwin.teacher_version_bob.pack.framework.Package;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BluetoothCommunicationActivity extends AppCompatActivity {
    private static final String BT_LOG_TAG = "BluetoothInfo";


    private enum Connected {False, Pending, True}

    private Connected connected = Connected.False;
    private MenuItem item_connection;
    private String deviceAddress;
    private SerialService serialService;
    private SerialListener listener;


    protected abstract void receive(byte[] data);

    protected final boolean isConnected() {
        return connected == Connected.True;
    }

    protected abstract Toolbar getToolbar();

    protected abstract int getContentView();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract String getDeviceAddress(Bundle savedInstanceState);

    protected abstract SerialListener getListener();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        setSupportActionBar(getToolbar());
        initialize(savedInstanceState);
        listener = getListener();

        boolean sus = bindService(new Intent(this, SerialService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("BindService", sus + "");
        Intent it = getIntent();
        deviceAddress = getDeviceAddress(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item_connection = menu.add("Connection");
        item_connection.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        setConnectionMenuItem(false);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == item_connection) {
            try {
                if (connected == Connected.False)
                    connect();
                else if (connected == Connected.True)
                    disconnect();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(BT_LOG_TAG, e.getMessage());
            }
            return true;
        }

        return false;
    }


    @Override
    public void onDestroy() {
        if (connected == Connected.True)
            disconnect();
        stopService(new Intent(this, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (serialService != null)
            serialService.attach(serialDataListener);
        else
            startService(new Intent(this, SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        if (serialService != null && !this.isChangingConfigurations())
            serialService.detach();
        super.onStop();
    }


    private void connect() throws IOException {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            Log.d(BT_LOG_TAG, "connecting...");
            SerialSocket socket = new SerialSocket(this, device, new ReadLineStrategy());
            serialService.connect(socket);
            connected = Connected.Pending;

        } catch (Exception e) {
            serialDataListener.onSerialConnectError(e);
            throw e;
        }
    }

    private void disconnect() {
        Log.d(BT_LOG_TAG, "disconnect");

        connected = Connected.False;
        serialService.disconnect();
        setConnectionMenuItem(false);
    }

    private void setConnectionMenuItem(boolean connected) {
        if (connected) {
            item_connection.setIcon(R.drawable.link_off);
            item_connection.setTitle("Disconnect");
        } else {
            item_connection.setIcon(R.drawable.link);
            item_connection.setTitle("Connect");
        }
    }

    protected void sendMessage(String msg) {
        Package pack = new LinePackage(new Base64Package(msg.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT));
        try {
            serialService.write(pack.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            serialService = ((SerialService.SerialBinder) binder).getService();
            serialService.attach(serialDataListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serialService = null;
        }
    };

    private final SerialListener serialDataListener = new SerialListener() {
        @Override
        public void onSerialConnect() {
            connected = Connected.True;
            Log.d(BT_LOG_TAG, "Bluetooth device connected");
            Toast.makeText(BluetoothCommunicationActivity.this, "Bluetooth device connected", Toast.LENGTH_SHORT).show();
            setConnectionMenuItem(true);
            if (listener != null)
                listener.onSerialConnect();

        }

        @Override
        public void onSerialConnectError(Exception e) {
            Log.e(BT_LOG_TAG, "Connection Error");
            Log.e(BT_LOG_TAG, e.getMessage());
            Toast.makeText(BluetoothCommunicationActivity.this, "Connection Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            disconnect();
            if (listener != null)
                listener.onSerialConnectError(e);
        }

        @Override
        public void onSerialRead(byte[] data) {
            receive(new Base64Package(data, Base64.DEFAULT).getDecoded());

            if (listener != null)
                listener.onSerialRead(data);
        }

        @Override
        public void onSerialIoError(Exception e) {
            Log.e(BT_LOG_TAG, "IO Error");
            Log.e(BT_LOG_TAG, e.getMessage());
            Toast.makeText(BluetoothCommunicationActivity.this, "IO Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            disconnect();

            if (listener != null)
                listener.onSerialIoError(e);
        }
    };

}
