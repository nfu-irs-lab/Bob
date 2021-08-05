package com.example.hiwin.teacher_version_bob.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.ReadLineStrategy;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.service.SerialService;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.SerialSocket;
import com.example.hiwin.teacher_version_bob.object.DataObject;
import com.example.hiwin.teacher_version_bob.object.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.view.FaceController;
import com.example.hiwin.teacher_version_bob.view.FaceFragment;
import com.example.hiwin.teacher_version_bob.view.FaceFragmentListener;
import com.example.hiwin.teacher_version_bob.view.ObjectShowerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static final String BT_LOG_TAG = "BluetoothInfo";
    private static final String THIS_LOG_TAG = "MainActivity";

    private enum Connected {False, Pending, True}

    private Connected connected = Connected.False;


    private MenuItem connection;

    private Context context;

    private String deviceAddress;
    private SerialService serialService;

    private ObjectSpeaker speaker;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        context = this;
        fragmentManager = getSupportFragmentManager();
        speaker = new ObjectSpeaker(this);

        boolean sus = bindService(new Intent(context, SerialService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("BindService", sus + "");
        Intent it = getIntent();
        deviceAddress = it.getStringExtra("address");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        connection = menu.getItem(0);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_connection) {
            if (connected == Connected.False)
                connect();
            else if (connected == Connected.True)
                disconnect();
        }

        return super.onOptionsItemSelected(item);
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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!initialStart && serialService != null) {
//            runOnUiThread(this::connect);
//        }
//
//        if (initialStart)
//            initialStart = false;
//    }


    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            Log.d(THIS_LOG_TAG, "connecting...");
            SerialSocket socket = new SerialSocket(context, device, new ReadLineStrategy());
            serialService.connect(socket);
            connected = Connected.Pending;

        } catch (Exception e) {
            serialDataListener.onSerialConnectError(e);
        }
    }

    private void disconnect() {
        Log.d(THIS_LOG_TAG, "disconnect");
        connected = Connected.False;
        serialService.disconnect();
        setConnectionMenuItem(false);
    }

    private void setConnectionMenuItem(boolean connected) {
        if (connected) {
            connection.setIcon(R.drawable.link_off);
            connection.setTitle("Disconnect");
        } else {
            connection.setIcon(R.drawable.link);
            connection.setTitle("Connected");
        }
    }

    void showObjectAndFace(final DataObject object) {
        final ObjectShowerFragment objectShowerFragment = new ObjectShowerFragment();
        objectShowerFragment.setObject(object);
        runOnUiThread(() -> postFragment(objectShowerFragment, "shower"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
        final FaceFragment faceFragment = new FaceFragment();
        faceFragment.setObject(object);
        faceFragment.setListener(new FaceFragmentListener() {
            @Override
            public void start(FaceController controller) {
                speaker.setSpeakerListener(() -> runOnUiThread(controller::hind));
                speaker.speak(object);
            }

            @Override
            public void complete(FaceController controller) {

            }
        });
        runOnUiThread(() -> postFragment(faceFragment, "face2"));

    }

    public void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frame, fragment, id);
        fragmentTransaction.commit();
    }

    private void send(String msg) {
        byte[] raw = Base64.encode(msg.getBytes(), Base64.DEFAULT);
        byte[] line = new byte[raw.length + 1];
        System.arraycopy(raw, 0, line, 0, raw.length);
//        line[line.length-1]='\n';

        try {
            serialService.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive(byte[] data) {
        try {
            String content = new String(Base64.decode(data, Base64.DEFAULT), StandardCharsets.UTF_8);
            Log.d(BT_LOG_TAG, "received string:");
            Log.d(BT_LOG_TAG, content);

            final JSONObject object;
            try {
                object = new JSONObject(content);
                showObjectAndFace((new DataObject.JSONParser()).parse(object, "zh_TW"));
            } catch (JSONException e) {
                Log.e(THIS_LOG_TAG, e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            Log.d(BT_LOG_TAG, "base64 decode error:");
            Log.d(BT_LOG_TAG, e.getMessage());
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
            setConnectionMenuItem(true);
        }

        @Override
        public void onSerialConnectError(Exception e) {
            Log.e(BT_LOG_TAG, "Connection Error");
            Log.e(BT_LOG_TAG, e.getMessage());
            disconnect();
        }

        @Override
        public void onSerialRead(byte[] data) {
            receive(data);
        }

        @Override
        public void onSerialIoError(Exception e) {
            Log.e(BT_LOG_TAG, "IO Error");
            Log.e(BT_LOG_TAG, e.getMessage());
            disconnect();
        }
    };

//    private final SerialDataListener serialDataListener = new SerialDataListener() {
//        @Override
//        protected void onStringDataReceived(String content) {
//            Log.d(BT_LOG_TAG, "received string:");
//            Log.d(BT_LOG_TAG, content);
//
//            final JSONObject object;
//            try {
//                object = new JSONObject(content);
//                showObjectAndFace((new DataObject.JSONParser()).parse(object, "zh_TW"));
//            } catch (JSONException e) {
//                Log.e(THIS_LOG_TAG, e.getMessage());
//            }
//        }
//
//        @Override
//        protected void onConnected() {
//            Log.d(BT_LOG_TAG, "Bluetooth device connected");
//            connection.setIcon(R.drawable.link_off);
//            connection.setTitle("Disconnect");
//        }
//
//        @Override
//        protected void onDisconnected() {
//            Log.d(BT_LOG_TAG, "Bluetooth device disconnected");
//            connection.setIcon(R.drawable.link);
//            connection.setTitle("Connected");
//        }
//
//        @Override
//        protected void onBase64DecodeError(IllegalArgumentException e) {
//            Log.e(BT_LOG_TAG, "Base64 decode error:");
//            Log.e(BT_LOG_TAG, e.getMessage());
//        }
//
//        @Override
//        protected void onIOError(Exception e) {
//            Log.e(BT_LOG_TAG, "IO Error");
//            Log.e(BT_LOG_TAG, e.getMessage());
//        }
//
//        @Override
//        protected void onConnectionError(Exception e) {
//            Log.e(BT_LOG_TAG, "Connection Error");
//            Log.e(BT_LOG_TAG, e.getMessage());
//        }
//
//        @Override
//        public void disconnect() {
//            onDisconnected();
//            if (serialService != null)
//                serialService.disconnect();
//        }
//    };
}
