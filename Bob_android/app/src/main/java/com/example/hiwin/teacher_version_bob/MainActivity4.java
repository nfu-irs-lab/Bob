package com.example.hiwin.teacher_version_bob;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.communication.SerialListener;
import com.example.hiwin.teacher_version_bob.communication.SerialService;
import com.example.hiwin.teacher_version_bob.communication.SerialSocket;
import com.example.hiwin.teacher_version_bob.object.JObject;
import com.example.hiwin.teacher_version_bob.object.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.view.FaceController;
import com.example.hiwin.teacher_version_bob.view.FaceFragment;
import com.example.hiwin.teacher_version_bob.view.FaceFragmentListener;
import com.example.hiwin.teacher_version_bob.view.ObjectShowerFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class MainActivity4 extends AppCompatActivity {


    private FragmentManager fragmentManager;

    private ObjectSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        speaker = new ObjectSpeaker(this);
        new Thread(() -> {
            try {
                JObject object = new JObject("car", "車", "My car", "我的車");
                showObjectAndFace(object);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void showObjectAndFace(final JObject object) throws InterruptedException {
        final ObjectShowerFragment objectShowerFragment = new ObjectShowerFragment();
        objectShowerFragment.setObject(object);
        runOnUiThread(() -> postFragment(objectShowerFragment, "shower"));
        Thread.sleep(5000);
        final FaceFragment faceFragment = new FaceFragment();
        faceFragment.setObject(object);
        faceFragment.setListener(new FaceFragmentListener() {
            @Override
            public void start(FaceController controller) {
                speaker.setSpeakerListener(controller::hind);
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
}
