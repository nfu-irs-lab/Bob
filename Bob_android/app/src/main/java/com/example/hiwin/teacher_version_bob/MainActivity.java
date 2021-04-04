package com.example.hiwin.teacher_version_bob;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hiwin.teacher_version_bob.communication.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.SerialService;
import com.example.hiwin.teacher_version_bob.communication.SerialSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
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

    private Context context;

    private String deviceAddress;
    private SerialService service;

    private boolean initialStart = true;
    private Connected connected = Connected.False;
    private DetectedObjectAdapter adapter;

    private TextToSpeech textToSpeech;

//    static class ViewHolder {
//        static class PromptView {
//            static TextView prompt_sentence;
//        }
//
//        static TextView name;
//        static TextView tr_name;
//        static TextView sentence;
//        static TextView tr_sentence;
//    }


    static class ViewHolder {
        static class Face {
            static ImageView left_eye;
            static ImageView right_eye;
            static ImageView mouth;
        }

        static class ObjectShower {
            static LinearLayout layout;
            static ImageView img;
            static TextView name;
            static TextView tr_name;
        }

    }
//    public FaceAdaptor face;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        context = this;

        ViewHolder.Face.left_eye = (ImageView) findViewById(R.id.main_left_eye);
        ViewHolder.Face.right_eye = (ImageView) findViewById(R.id.main_right_eye);
        ViewHolder.Face.mouth = (ImageView) findViewById(R.id.main_mouth);
//        ViewHolder.Face.left_eye.setVisibility(View.INVISIBLE);
//        ViewHolder.Face.right_eye.setVisibility(View.INVISIBLE);
//        ViewHolder.Face.mouth.setVisibility(View.INVISIBLE);

        ViewHolder.ObjectShower.layout = (LinearLayout) findViewById(R.id.object_layout);
        ViewHolder.ObjectShower.name = (TextView) findViewById(R.id.object_name);
        ViewHolder.ObjectShower.tr_name = (TextView) findViewById(R.id.object_tr_name);
        ViewHolder.ObjectShower.img = (ImageView) findViewById(R.id.object_img);

        ViewHolder.ObjectShower.layout.setVisibility(View.INVISIBLE);



        ViewHolder.Face.left_eye.setVisibility(View.VISIBLE);
        ViewHolder.Face.right_eye.setVisibility(View.VISIBLE);
        ViewHolder.Face.mouth.setVisibility(View.VISIBLE);

//        face=new FaceAdaptor(ViewHolder.Face.left_eye,ViewHolder.Face.right_eye,ViewHolder.Face.mouth);
//        face.hindFace();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    final AnimatorSet animatorSet=face.setSeeLeftAndRight();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            animatorSet.start();
//                        }
//                    });
//
//                    try {
//                        Thread.sleep(17000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                }
//            }
//        }).start();

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
            BTLog('d', "connecting...");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(context, device);
            service.connect(socket);

        } catch (Exception e) {
            serialListener.onSerialConnectError(e);
        }
    }

    private void disconnect() {
        BTLog('d', "disconnected");
        connected = Connected.False;
        setMenuConnectionStatus(connected);
        service.disconnect();
    }


    void BTLog(char tag, String str) {
        switch (tag) {
            case 'v':
                Log.v("BluetoothLog", str);
                break;
            case 'd':
                Log.d("BluetoothLog", str);
                break;
            case 'e':
                Log.e("BluetoothLog", str);
                break;
            default:
                break;
        }
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

    private void OnMessageReceived(String str) throws JSONException {
//        face.hindFace();
        BTLog('d', str);
        JSONArray array = new JSONArray(str);
        final JSONObject object = array.getJSONObject(0);
        JSONArray languages = object.getJSONArray("languages");
        final JSONObject zhTW = languages.getJSONObject(0);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ViewHolder.ObjectShower.layout.setVisibility(View.VISIBLE);
                    ViewHolder.ObjectShower.name.setText(object.getString("name"));
                    ViewHolder.ObjectShower.tr_name.setText(zhTW.getString("tr_name"));
                    ViewHolder.ObjectShower.img.setImageDrawable(getDrawableByString(object.getString("name")));
//                    Thread.sleep(5000);
//                    ViewHolder.ObjectShower.layout.setVisibility(View.INVISIBLE);
//
//                    AnimatorSet animatorSet=face.setSeeLeftAndRight();
//                    face.showFace();
//                    animatorSet.start();
                    if (!textToSpeech.isSpeaking()) {
                        textToSpeech.setLanguage(Locale.US);
                        textToSpeech.speak(object.getString("name"), TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.speak(object.getString("name"), TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.speak(object.getString("name"), TextToSpeech.QUEUE_ADD, null);

                        textToSpeech.setLanguage(Locale.TAIWAN);
                        textToSpeech.speak(zhTW.getString("tr_name"), TextToSpeech.QUEUE_ADD, null);

                        textToSpeech.setLanguage(Locale.US);
                        textToSpeech.speak(object.getString("sentence"), TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.speak(object.getString("sentence"), TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.speak(object.getString("sentence"), TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.setLanguage(Locale.TAIWAN);
                        textToSpeech.speak(zhTW.getString("tr_sentence"), TextToSpeech.QUEUE_ADD, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    Drawable getDrawableByString(String str){
        switch (str){
            case "car":
                return getResources().getDrawable(R.drawable.ic_eco_car);

            case "knife":
                return getResources().getDrawable(R.drawable.ic_french_knife);

            case "cake":
                return getResources().getDrawable(R.drawable.ic_birthday_cake);
        }
        return null;
    }
    private void send_msg(String msg) {
        byte[] raw = Base64.encode(msg.getBytes(), Base64.URL_SAFE);
        byte[] line = new byte[raw.length + 1];
        System.arraycopy(raw, 0, line, 0, raw.length);
//        line[line.length-1]='\n';

        try {
            service.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String dumpArray(byte[] array) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(",");
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }


    SerialListener serialListener = new SerialListener() {
        @Override
        public void onSerialConnect() {
            BTLog('d', "connected");
            send_msg("hello");
            connected = Connected.True;
            setMenuConnectionStatus(connected);
        }

        @Override
        public void onSerialConnectError(Exception e) {
            BTLog('e', "connection failed: " + e.getMessage());
            e.printStackTrace();
            connection.setIcon(R.drawable.link_off);
            disconnect();
        }

        LinkedList<Byte> pre_data = new LinkedList<>();

        @Override
        public void onSerialRead(byte[] data) {
            BTLog('v', dumpArray(data));

            for (byte datum : data) {
                pre_data.add(datum);
            }

            LinkedList<Byte> buffer = new LinkedList<>(pre_data);
//            for (int i = 0; i < pre_data.size(); i++) {
//                buffer.add(i, pre_data.get(i));
//            }

            StringBuffer sb;
            do {
                sb = new StringBuffer();
                int original_size = buffer.size();
                for (int i = 0; i < original_size; i++) {
                    char chr = (char) (byte) buffer.pollFirst();
                    if (chr == 0xa) {
                        pre_data = buffer;
                        final String recv_data = sb.toString();
                        BTLog('v', recv_data);
                        sb = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    byte[] raw_bytes = Base64.decode(recv_data.getBytes(StandardCharsets.UTF_8), Base64.URL_SAFE);

                                    String msg = new String(raw_bytes, StandardCharsets.UTF_8);
                                    OnMessageReceived(msg);
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }
                        });
                        break;

                    } else {
                        sb.append(chr);
                    }
                }
            } while (sb == null);

        }

        @Override
        public void onSerialIoError(Exception e) {
            BTLog('e', "connection lost: " + e.getMessage());
            disconnect();
        }

    };

}
