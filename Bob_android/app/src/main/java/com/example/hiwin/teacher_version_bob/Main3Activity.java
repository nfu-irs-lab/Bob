package com.example.hiwin.teacher_version_bob;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

import android.widget.Toast;
import java.util.ArrayList;


public class Main3Activity extends Activity {

    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:40:8D:67";

    String[] Sent = {"how are you", "hello"};
    String[] Response = {"I'm fine,thank you", "HI I am happiness, I am a English Teaching robot","i'm two years old","It is sunday","I'm from national formosa university","I'm standing and watching you","I had a nice day"};
    String Wrong = "I don't know What you saying";
    private Button btnDialog,btnBack;
    private TextView textView, tv2;
    TextToSpeech t2;
    byte[] msgBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        btnDialog = (Button) this.findViewById(R.id.btn1);
        btnBack = (Button)this.findViewById(R.id.button2);
        textView = (TextView) this.findViewById(R.id.tv1);
        //tv2 = (TextView) this.findViewById(R.id.tv2);

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
        t2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t2.setLanguage(Locale.US);    //設定語言為英文
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e("TTS", "This Language is not supported");
                    } else {
                        t2.setPitch(1);    //語調(1為正常語調；0.5比正常語調低一倍；2比正常語調高一倍)
                        t2.setSpeechRate(0.7f);    //速度(1為正常速度；0.5比正常速度慢一倍；2比正常速度快一倍)
                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

    }

    public void Click(View view) {
                //透過 Intent 的方式開啟內建的語音辨識 Activity...
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

                startActivityForResult(intent, 1);
        tx0("d");

    }

    public void clickback(View view){
        Intent intent = new Intent();
        intent.setClass(Main3Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
        resetConnection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textView.setText(result.get(0));

                if ("how are you".equals(result.get(0))) {
                    Toast.makeText(this, Response[0], Toast.LENGTH_LONG).show();
                    t2.speak(Response[0], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("hello".equals(result.get(0))) {
                    Toast.makeText(this, Response[1], Toast.LENGTH_LONG).show();
                    t2.speak(Response[1], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("how old are you".equals(result.get(0))) {
                    Toast.makeText(this, Response[2], Toast.LENGTH_LONG).show();
                    t2.speak(Response[2], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("what is the weather today".equals(result.get(0))) {
                    Toast.makeText(this, Response[3], Toast.LENGTH_LONG).show();
                    t2.speak(Response[3], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("where are you from".equals(result.get(0))) {
                    Toast.makeText(this, Response[4], Toast.LENGTH_LONG).show();
                    t2.speak(Response[4], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("what are you doing".equals(result.get(0))) {
                    Toast.makeText(this, Response[5], Toast.LENGTH_LONG).show();
                    t2.speak(Response[5], TextToSpeech.QUEUE_FLUSH, null);
                } else if ("how is going on".equals(result.get(0))) {
                    Toast.makeText(this, Response[6], Toast.LENGTH_LONG).show();
                    t2.speak(Response[6], TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    Toast.makeText(this, Wrong, Toast.LENGTH_LONG).show();
                    t2.speak(Wrong, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        }
    }

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
            //btSocket = null;
        //}

    }

}