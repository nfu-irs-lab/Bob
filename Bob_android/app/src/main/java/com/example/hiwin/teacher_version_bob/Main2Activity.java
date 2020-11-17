package com.example.hiwin.teacher_version_bob;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Main2Activity extends Activity {
    private static final String NAME = "BluetoothChat";//20161011
    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null; //in


    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:40:8D:67";

    byte[] msgBuffer;
    TextToSpeech t1;
    //EditText ed1;
    String ed1="HI I'm happiness, I'm a English Teaching robot";
    TextView T1,T2,T3,T4,T5,T6,T7,T8,T9,T10;
    Button b1;
    String[] name = {"bow","cry","knock","fly","run","read","goodbye","eat","driver","walk"};
    String[] Sentence = {"Bow...She Bowed her thanks.","Cry...Don't cry.You're a big boy now.",
            "Knock...I am knocking the door.","Fly...I will fly to Taiwan tomorrow.",
            "Run...How do you run so fast","Read...Keep reading those books every night.",
            "Goodbye...Let's say goodbye.","Eat...Don't eat too fast.","Driver...I'm a taxi driver.",
            "Walk...He arose and walked to the window."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //ed1=(EditText)findViewById(R.id.editText);
        //b1=(Button)findViewById(R.id.button);
        T1 = (TextView)findViewById(R.id.textView1);
        T2 = (TextView)findViewById(R.id.textView2);
        T3 = (TextView)findViewById(R.id.textView3);
        T4 = (TextView)findViewById(R.id.textView4);
        T5 = (TextView)findViewById(R.id.textView5);
        T6 = (TextView)findViewById(R.id.textView6);
        T7 = (TextView)findViewById(R.id.textView7);
        T8 = (TextView)findViewById(R.id.textView8);
        T9 = (TextView)findViewById(R.id.textView9);
        T10 = (TextView)findViewById(R.id.textView10);


        if (D)
            Log.e(TAG, "+++ON CREATE +++");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable your Bluetooth and re-run this program.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (D)
            Log.e(TAG, "+++ DONE IN ON CREATE, GOT LOCAL BT ADAPTER +++");



        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t1.setLanguage(Locale.US);    //設定語言為英文
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e("TTS", "This Language is not supported");
                    } else {
                        t1.setPitch(1);    //語調(1為正常語調；0.5比正常語調低一倍；2比正常語調高一倍)
                        t1.setSpeechRate(0.7f);    //速度(1為正常速度；0.5比正常速度慢一倍；2比正常速度快一倍)
                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
        T1.setBackgroundColor(0x00000000);
        T2.setBackgroundColor(0x00000000);
        T3.setBackgroundColor(0x00000000);
        T4.setBackgroundColor(0x00000000);
        T5.setBackgroundColor(0x00000000);
        T6.setBackgroundColor(0x00000000);
        T7.setBackgroundColor(0x00000000);
        T8.setBackgroundColor(0x00000000);
        T9.setBackgroundColor(0x00000000);
        T10.setBackgroundColor(0x00000000);




    }

    public void clickMenuItem(View view) {
        switch ( view.getId()) {
            case R.id.textView1:
                Toast.makeText(this, Sentence[0], Toast.LENGTH_LONG).show();
                //t1.speak(Sentence[0], TextToSpeech.QUEUE_FLUSH, null);
                t1.speak(Sentence[0], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView2:
                Toast.makeText(this, Sentence[1], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[1], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView3:
                Toast.makeText(this, Sentence[2], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[2], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView4:
                Toast.makeText(this, Sentence[3], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[3], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView5:
                Toast.makeText(this, Sentence[4], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[4], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView6:
                Toast.makeText(this, Sentence[5], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[5], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView7:
                Toast.makeText(this, Sentence[6], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[6], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView8:
                Toast.makeText(this, Sentence[7], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[7], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView9:
                Toast.makeText(this, Sentence[8], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[8], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.textView10:
                Toast.makeText(this, Sentence[9], Toast.LENGTH_LONG).show();
                t1.speak(Sentence[9], TextToSpeech.QUEUE_FLUSH, null);
                tx0("c");
                break;
            case R.id.button:
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                resetConnection();
                break;
        }

    }
    //public void onPause(){

        //super.onPause();
    //}
    public void onStart() {
        super.onStart();
        if (D)

            Log.e(TAG, "++ ON START ++");
    }


    public void onResume() {
        super.onResume();
        if (D) {
            Log.e(TAG, "+ ON RESUME +");
            Log.e(TAG, "+ ABOUT TO ATTEMPT CLIENT CONNECT +");

        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {

            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {

            Log.e(TAG, "ON RESUME: Socket creation failed.", e);

        }
        mBluetoothAdapter.cancelDiscovery();
        try {

            btSocket.connect();

            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");

        } catch (IOException e) {

            try {
                btSocket.close();

            } catch (IOException e2) {

                Log.e(TAG, "ON RESUME: Unable to close socket during connection failure", e2);
            }

        }
        if (D)
            Log.e(TAG, "+ ABOUT TO SAY SOMETHING TO SERVER +");
    }

    public void onPause() {

        super.onPause();
        if (D)
            Log.e(TAG, "- ON PAUSE -");
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }

        }
        try {
            btSocket.close();
        } catch (IOException e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        }
    }


    public void tx0(String message) {
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Output stream cream failed.", e);
        }

        ///message = "b";  // 傳送點
        msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME:Exception during write.", e);

        }


    }
    private void resetConnection() {
        if (inStream != null) {
            try {inStream.close();} catch (Exception e) {}
            inStream = null;
        }

        if (outStream != null) {
            try {outStream.close();} catch (Exception e) {}
            outStream = null;
        }

        //if (btSocket != null) {
            //try {btSocket.close();} catch (Exception e) {}
           // btSocket = null;
        //}

    }
}
