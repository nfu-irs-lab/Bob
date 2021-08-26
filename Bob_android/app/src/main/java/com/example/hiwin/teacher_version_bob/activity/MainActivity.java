package com.example.hiwin.teacher_version_bob.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.ReadLineStrategy;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.service.SerialService;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.concrete.SerialSocket;
import com.example.hiwin.teacher_version_bob.data.concrete.pack.Base64Package;
import com.example.hiwin.teacher_version_bob.data.concrete.pack.LinePackage;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;
import com.example.hiwin.teacher_version_bob.data.concrete.object.parser.JSONObjectParser;
import com.example.hiwin.teacher_version_bob.data.framework.pack.Package;

import com.example.hiwin.teacher_version_bob.fragment.DefaultFragment;
import com.example.hiwin.teacher_version_bob.fragment.FragmentListener;
import com.example.hiwin.teacher_version_bob.handler.ReceiveFragmentHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static final String BT_LOG_TAG = "BluetoothInfo";
    private static final String THIS_LOG_TAG = "MainActivity";
    private boolean isDetecting;

    private enum Connected {False, Pending, True}

    private Connected connected = Connected.False;


    private MenuItem connection, detect;

    private Context context;

    private String deviceAddress;
    private SerialService serialService;

    private ReceiveFragmentHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        context = this;
        handler = new ReceiveFragmentHandler(context, Looper.getMainLooper(), getSupportFragmentManager()) {
            @Override
            protected void onComplete() {
                detect_pause();
                showDefault();
            }
        };

        boolean sus = bindService(new Intent(context, SerialService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("BindService", sus + "");
        Intent it = getIntent();
        deviceAddress = it.getStringExtra("address");
        showDefault();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        connection = menu.getItem(0);
        detect=menu.getItem(1);
        setConnectionMenuItem(false);
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
        }else if(item.getItemId()==R.id.menu_main_detect){
            if(connected==Connected.True){
                if(isDetecting) {
                    detect_pause();
                    isDetecting=false;
                }else{
                    detect_start();
                    isDetecting=true;
                }
            }else {
                Toast.makeText(this,"Not connected",Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void detect_pause() {
        setDetectMenuItem(false);
        sendMessage("PAUSE_DETECT");
    }

    private void detect_start() {
        setDetectMenuItem(true);
        sendMessage("START_DETECT");
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
        detect_pause();
        serialService.disconnect();
        setConnectionMenuItem(false);
    }

    private void setConnectionMenuItem(boolean connected) {
        if (connected) {
            connection.setIcon(R.drawable.link_off);
            connection.setTitle("Disconnect");
        } else {
            connection.setIcon(R.drawable.link);
            connection.setTitle("Connect");
        }
    }

    private void setDetectMenuItem(boolean isDetecting) {
        if (isDetecting) {
            detect.setTitle("Pause");
        } else {
            detect.setTitle("Start");
        }
    }

    private void showObjectAndFace(final DataObject object) {
        Message msg = new Message();
        msg.what = ReceiveFragmentHandler.CODE_RECEIVE;
        msg.obj = object;
        handler.sendMessage(msg);

    }

    private void sendMessage(String msg) {
        Package pack = new LinePackage(new Base64Package(msg.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT));
//        Package pack = new LinePackage((msg.getBytes(StandardCharsets.UTF_8)));
        try {
            serialService.write(pack.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive(byte[] data) {
        try {
            String content = new String(new Base64Package(data, Base64.DEFAULT).getDecoded(), StandardCharsets.UTF_8);
            Log.d(BT_LOG_TAG, "received string:");
            Log.d(BT_LOG_TAG, content);

            try {
                detect_pause();
                showObjectAndFace((new JSONObjectParser("zh_TW")).parse(new JSONObject(content)));
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
            sendMessage("Hello Package");
            setConnectionMenuItem(true);
            detect_pause();
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

    private void showDefault(){
        final DefaultFragment fragment=new DefaultFragment();
        fragment.setListener(new FragmentListener() {
            @Override
            public void start() {
            }

            @Override
            public void end() {
                detect_start();
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frame, fragment, "default");
        fragmentTransaction.commit();
    }
}
