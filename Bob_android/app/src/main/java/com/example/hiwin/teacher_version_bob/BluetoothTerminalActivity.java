package com.example.hiwin.teacher_version_bob;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hiwin.teacher_version_bob.communication.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.SerialService;
import com.example.hiwin.teacher_version_bob.communication.SerialSocket;

import java.util.Locale;

public class BluetoothTerminalActivity extends AppCompatActivity implements ServiceConnection, SerialListener {



    /*
        reference:
            https://developer.android.com/guide/topics/connectivity/bluetooth.html
            https://xnfood.com.tw/activity-life-cycle/
            https://zh.wikipedia.org/wiki/%E8%97%8D%E7%89%99%E8%A6%8F%E7%AF%84#%E5%BA%8F%E5%88%97%E5%9F%A0%E8%A6%8F%E7%AF%84_%EF%BC%88SPP%EF%BC%89
            https://developer.android.com/guide/components/activities/activity-lifecycle
            https://developer.android.com/guide/components/fragments
            https://developer.android.com/guide/components/services
            https://www.tutorialspoint.com/android/android_text_to_speech.htm
     */

    private enum Connected {False, Pending, True}

    private static final byte[] ENABLE_PATTERN_RECOGNITION = {(byte) 0xff, (byte) 0xef, (byte) 0x01, (byte) 0x00, (~1)};



    private TextView receiveText;
    private TextView sendText;
    private Context context;

    private String deviceAddress;
    private SerialService service;

    private boolean initialStart=true;
    private Connected connected = Connected.False;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_terminal);

        context = this;

//        OnAttach
        boolean sus=bindService(new Intent(context, SerialService.class), this, Context.BIND_AUTO_CREATE);
        Log.d("BindService",sus+"");
//        OnCreate
        Intent it = getIntent();
        deviceAddress = it.getStringExtra("address");

//        OnCreateView
        receiveText = (TextView) findViewById(R.id.receive_text);
        sendText = (TextView) findViewById(R.id.send_text);

        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (service != null)
            service.attach(this);
        else
            startService(new Intent(context, SerialService.class));
    }

    public void OnSendClick(View v) {
        send(sendText.getText().toString());
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart) {
            initialStart = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            });
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        e.printStackTrace();
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (initialStart && service != null) {
            initialStart = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(context,SerialService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unbindService(this);
        } catch (Exception ignored) {

        }
    }

    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(context, device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
    }

    private void send(String msg) {
        if (connected != Connected.True) {
            Toast.makeText(BluetoothTerminalActivity.this, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] data = msg.getBytes();
            status("send:\t"+msg);
            textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
            service.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void receive(byte[] data) {
        String msg = new String(data);
        status("receive:\t"+msg);
        textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

    }

    void status(String str) {
        Log.d("BluetoothLog", str);
        receiveText.append(str+"\n");
    }
}
