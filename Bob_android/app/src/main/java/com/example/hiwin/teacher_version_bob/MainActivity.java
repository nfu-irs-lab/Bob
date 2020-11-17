package com.example.hiwin.teacher_version_bob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String NAME = "BluetoothChat";//20161011
    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket MbtSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null; //in

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:21:13:04:2A:F0";

    String[] Sent = {"how are you", "hello"};
    String[] Response = {"I'm fine,thank you", "Hello, I am a English Teaching robot","i'm two years old","It is sunday","I'm from national formosa university","I'm standing and watching you","I had a nice day"};
    String Wrong = "I don't know What you saying";

    byte[] msgBuffer;
    private Button b0;
    private Button b1;
    private Button Voc;
    private Button Dai;
    private Button vhot, vsing, vread, vcold, vcool, vwarm, vgohome, back1, voctest, conver;
    private TextView text17, tttt;
    TextToSpeech t2;
    Integer aaa = 0 , bbb = 0 ;

    String message_tx[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b0 = (Button) findViewById(R.id.DANCE);
        b1 = (Button) findViewById(R.id.HOME);
        Voc = (Button) findViewById(R.id.Voc);
        Dai = (Button) findViewById(R.id.DDD);
        vhot = (Button) findViewById(R.id.VHot);
        vsing = (Button) findViewById(R.id.Vsing);
        vread = (Button) findViewById(R.id.Vread);
        vcold = (Button) findViewById(R.id.Vcold);
        vwarm = (Button) findViewById(R.id.Vwarm);
        vgohome = (Button) findViewById(R.id.Vgohome);
        vcool = (Button) findViewById(R.id.Vcool);
        back1 = (Button) findViewById(R.id.back1);
        voctest = (Button) findViewById(R.id.Voctest);
        conver = (Button) findViewById(R.id.Conver);
        text17 = (TextView) findViewById(R.id.textView17);
        tttt =  (TextView) findViewById(R.id.textView18);

        b0.setOnClickListener(this);
        b1.setOnClickListener(this);
        Voc.setOnClickListener(this);
        Dai.setOnClickListener(this);
        vhot.setOnClickListener(this);
        back1.setOnClickListener(this);
        vcold.setOnClickListener(this);
        vcool.setOnClickListener(this);
        vwarm.setOnClickListener(this);
        vread.setOnClickListener(this);
        vgohome.setOnClickListener(this);
        vsing.setOnClickListener(this);
        voctest.setOnClickListener(this);
        conver.setOnClickListener(this);
        text17.setOnClickListener(this);
        tttt.setOnClickListener(this);


        this.vhot.setVisibility(View.INVISIBLE);
        this.vsing.setVisibility(View.INVISIBLE);
        this.vread.setVisibility(View.INVISIBLE);
        this.vcold.setVisibility(View.INVISIBLE);
        this.vwarm.setVisibility(View.INVISIBLE);
        this.vgohome.setVisibility(View.INVISIBLE);
        this.vcool.setVisibility(View.INVISIBLE);
        this.back1.setVisibility(View.INVISIBLE);



        if (D) {
            Log.e(TAG, "+++ON CREATE +++");
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
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
                        t2.setSpeechRate(0.8f);    //速度(1為正常速度；0.5比正常速度慢一倍；2比正常速度快一倍)
                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        MediaPlayer mper = MediaPlayer.create(this, R.raw.shot);
        mper.reset();
        mper.start();
    }

    @Override
    public void onClick(View view) {
        //Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.DANCE:
                tx0("a ");
                try {
                    Uri uri4 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.produce);
                    VideoView videoView4 = new VideoView(this);
                    setContentView(videoView4);
                    videoView4.setVideoURI(uri4);
                    videoView4.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(false);
                        }
                    });

                    videoView4.start();
                }catch (Exception e){}
                break;
            case R.id.HOME:
                tx0("b");
                break;
            case R.id.Voc:
                this.vhot.setVisibility(View.VISIBLE);
                this.vsing.setVisibility(View.VISIBLE);
                this.vread.setVisibility(View.VISIBLE);
                this.vcold.setVisibility(View.VISIBLE);
                this.vwarm.setVisibility(View.VISIBLE);
                this.vgohome.setVisibility(View.VISIBLE);
                this.vcool.setVisibility(View.VISIBLE);////
                this.back1.setVisibility(View.VISIBLE);
                this.Voc.setVisibility(View.INVISIBLE);
                this.voctest.setVisibility(View.INVISIBLE);
                this.conver.setVisibility(View.INVISIBLE);
                this.text17.setVisibility(View.INVISIBLE);

                break;
            case R.id.back1:
                this.vhot.setVisibility(View.INVISIBLE);
                this.vsing.setVisibility(View.INVISIBLE);
                this.vread.setVisibility(View.INVISIBLE);
                this.vcold.setVisibility(View.INVISIBLE);
                this.vwarm.setVisibility(View.INVISIBLE);
                this.vgohome.setVisibility(View.INVISIBLE);
                this.vcool.setVisibility(View.INVISIBLE);////
                this.back1.setVisibility(View.INVISIBLE);
                this.Voc.setVisibility(View.VISIBLE);
                this.voctest.setVisibility(View.VISIBLE);
                this.conver.setVisibility(View.VISIBLE);
                this.text17.setVisibility(View.VISIBLE);
                break;
            case R.id.DDD:
                try{
                    Uri uri = Uri.parse("android.resource://"+getPackageName() + "/" + R.raw.a01_1207);
                    Uri uri2 = Uri.parse("android.resource://"+getPackageName() + "/" + R.raw.a02_1207);
                    Uri uri3 = Uri.parse("android.resource://"+getPackageName() + "/" + R.raw.a03_1207);

                    VideoView videoView = new VideoView(this);
                    VideoView videoView2 = new VideoView(this);
                    VideoView videoView3 = new VideoView(this);

                    setContentView(videoView);
                    setContentView(videoView2);
                    setContentView(videoView3);

                    videoView.setVideoURI(uri);
                    videoView2.setVideoURI(uri2);
                    videoView3.setVideoURI(uri3);

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(false);
                        }
                    });
                    videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(false);
                        }
                    });
                    videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(false);
                        }
                    });


                    videoView.start();
                    videoView2.start();
                    videoView3.start();


                }catch (Exception e){}
                break;
            case R.id.VHot:
                tx0("c");
                t2.speak("HOT", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vcold:
                tx0("d");
                t2.speak("Cold", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vcool:
                tx0("e");
                t2.speak("cool", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vwarm:
                tx0("f");
                t2.speak("warm", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vread:
                tx0("g");
                t2.speak(" Read, I am reading book!", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vgohome:
                tx0("h");
                t2.speak("go home, I want to go home now!", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Vsing:
                tx0("i");
                t2.speak("sing", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.Conver:
                //透過 Intent 的方式開啟內建的語音辨識 Activity...
                Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent2.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

                startActivityForResult(intent2, 1);
                if (aaa ==1){
                    tx0("c");
                }

                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                tttt.setText(result.get(0));
                if ("how are you".equals(result.get(0))) {
                    Toast.makeText(this, Response[0], Toast.LENGTH_LONG).show();
                    t2.speak(Response[0], TextToSpeech.QUEUE_FLUSH, null);
                    aaa = 1;
                } else if ("hello".equals(result.get(0))) {
                    Toast.makeText(this, Response[1], Toast.LENGTH_LONG).show();
                    t2.speak(Response[1], TextToSpeech.QUEUE_FLUSH, null);
                    bbb = 2;
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
                } else if ("哈囉".equals(result.get(0))) {
                    Toast.makeText(this, Response[1], Toast.LENGTH_LONG).show();
                        t2.speak(Response[1], TextToSpeech.QUEUE_FLUSH, null);
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

            MbtSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {

            Log.e(TAG, "ON RESUME: Socket creation failed.", e);

        }
        mBluetoothAdapter.cancelDiscovery();
        try {

            MbtSocket.connect();

            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
        } catch (IOException e) {
            try {
                MbtSocket.close();
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
            MbtSocket.close();
        } catch (IOException e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        }
    }


    public void tx0(String message) {
        try {
            outStream = MbtSocket.getOutputStream();
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
            }
}