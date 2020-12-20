package com.example.hiwin.teacher_version_bob;

import android.annotation.SuppressLint;
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
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hiwin.teacher_version_bob.communication.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.SerialService;
import com.example.hiwin.teacher_version_bob.communication.SerialSocket;
import com.example.hiwin.teacher_version_bob.protocol.ProtocolSocket;
import com.example.hiwin.teacher_version_bob.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.core.ProtocolListener;
import com.example.hiwin.teacher_version_bob.protocol.core.ServerHelloPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ServiceConnection {



    /*
        reference:
            https://developer.android.com/guide/topics/connectivity/bluetooth.html
            https://xnfood.com.tw/activity-life-cycle/
            https://zh.wikipedia.org/wiki/%E8%97%8D%E7%89%99%E8%A6%8F%E7%AF%84#%E5%BA%8F%E5%88%97%E5%9F%A0%E8%A6%8F%E7%AF%84_%EF%BC%88SPP%EF%BC%89
            https://developer.android.com/guide/components/activities/activity-lifecycle
            https://developer.android.com/guide/components/fragments
            https://developer.android.com/guide/components/services
            https://www.tutorialspoint.com/android/android_text_to_speech.htm
            https://medium.com/verybuy-dev/android-%E8%A3%A1%E7%9A%84%E7%B4%84%E6%9D%9F%E6%80%96%E5%B1%80-constraintlayout-6225227945ab
            https://materialdesignicons.com/
     */

    private enum Connected {False, Pending, True}


    private MenuItem connection;

    private Toolbar toolbar;
    private Context context;


    private String deviceAddress;
    private SerialService service;

    private boolean initialStart = true;
    private Connected connected = Connected.False;
    //    private ClientProtocol clientProtocol;
    ProtocolSocket protocolSocket;
    private ListView listView;
    private DetectedObjectAdapter adapter;
    private ArrayList<HashMap<String, Object>> detectedObjects = new ArrayList<>();

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        context = this;

        adapter = new DetectedObjectAdapter(context, detectedObjects);

        listView = (ListView) findViewById(R.id.main_object_list);
        listView.setAdapter(adapter);

        protocolSocket = new ProtocolSocket();
        protocolSocket.connect(new ProtocolListener() {
            @Override
            public void OnProtocolConnected() {

            }

            @Override
            public void OnProtocolDisconnected() {

            }

            @Override
            public void OnReceiveDataPackage(byte[] data) {
                Log.d("MainActivityLog",BytesInHexString(data));

                String base64str=new String(data, StandardCharsets.UTF_8);
                byte[] rawBytes= Base64.decode(base64str,Base64.DEFAULT);
                final String rawString=new String(rawBytes, StandardCharsets.UTF_8);

                Log.d("MainActivityLog",rawString);
                Log.d("ProtocolLog",rawString);
                                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter!=null){
                            try {
                                JSONArray array=new JSONArray(rawString);
                                detectedObjects.clear();

                                for(int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);
                                    HashMap<String,Object> t=new HashMap<>();
                                    t.put("name",object.getString("name"));
                                    t.put("number",object.getString("number"));
                                    detectedObjects.add(t);
                                }
                                adapter=new DetectedObjectAdapter(context,detectedObjects);

                                listView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }

            @Override
            public void OnWrite(byte[] data) {
                try {

                    status("[Write]");
                    status(BytesInHexString(data));
                    status("<Write>");
                    service.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

//        OnAttach
        boolean sus = bindService(new Intent(context, SerialService.class), this, Context.BIND_AUTO_CREATE);
        Log.d("BindService", sus + "");
//        OnCreate
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
    protected void onStart() {
        super.onStart();
        if (service != null)
            service.attach(serialListener);
        else
            startService(new Intent(context, SerialService.class));
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(serialListener);

//        clientProtocol=new ClientProtocol();
//        clientProtocol.attach(new ProtocolListener() {
//            @Override
//            public void OnProtocolConnected() {
//                Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
//                setMenuConnectionStatus(connected);
//            }
//
//            @Override
//            public void OnProtocolDisconnected() {
//
//            }
//
//            @Override
//            public void OnConnectionRejected(ServerHelloPackage.StatusCode statusCode) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                });
//            }
//
//
//
//            @Override
//            public void OnReceiveData(byte[] data) {
//
//                String base64str=new String(data, StandardCharsets.UTF_8);
//                byte[] rawBytes= Base64.decode(base64str,Base64.DEFAULT);
//                final String rawString=new String(rawBytes, StandardCharsets.UTF_8);
//                Log.d("ProtocolLog",rawString);
//                                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(adapter!=null){
//                            try {
//                                JSONArray array=new JSONArray(rawString);
//                                detectedObjects.clear();
//
//                                for(int i=0;i<array.length();i++){
//                                    JSONObject object=array.getJSONObject(i);
//                                    HashMap<String,Object> t=new HashMap<>();
//                                    t.put("name",object.getString("name"));
//                                    t.put("number",object.getString("number"));
//                                    detectedObjects.add(t);
//                                }
//                                adapter=new DetectedObjectAdapter(context,detectedObjects);
//
//                                listView.setAdapter(adapter);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//
//            }
//        });


        if (initialStart) {
            initialStart = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMenuConnectionStatus(connected);
                }
            });
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (initialStart && service != null) {
            initialStart = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    connect();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (textToSpeech != null)
            textToSpeech.shutdown();

        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(context, SerialService.class));

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
            serialListener.onSerialConnectError(e);
        }
    }

    private void disconnect() {
        status("disconnected");
        connected = Connected.False;
        setMenuConnectionStatus(connected);
        service.disconnect();
    }


    void status(String str) {
        Log.d("BluetoothLog", str);
    }

    void setMenuConnectionStatus(Connected connected) {
        if (connected == Connected.False) {
            connection.setIcon(R.drawable.link);
            connection.setTitle("Connected");
        } else if (connected == Connected.True) {
            connection.setIcon(R.drawable.link_off);
            connection.setTitle("Disconnected");
        }
    }

    SerialListener serialListener = new SerialListener() {
        @Override
        public void onSerialConnect() {
            status("connected");
            connected = Connected.True;
            protocolSocket.write(new ClientHelloPackage());

        }

        @Override
        public void onSerialConnectError(Exception e) {
            status("connection failed: " + e.getMessage());
            e.printStackTrace();
            connection.setIcon(R.drawable.link_off);
            disconnect();
//        finish();
        }

        @Override
        public void onSerialRead(byte[] data) {
            status("[Read]");
            status(BytesInHexString(data));
            protocolSocket.put(data);
            status("<Read>");
        }

        @Override
        public void onSerialIoError(Exception e) {
            status("connection lost: " + e.getMessage());
            disconnect();
//        finish();
        }
    };

    String BytesInHexString(byte[] raw) {
        StringBuffer sb = new StringBuffer();

        sb.append("{");
        for (byte b : raw) {
            sb.append("0x").append(Integer.toHexString(b & 0xFF)).append(",");
        }
        sb.append("}");
        return sb.toString();
    }
}
