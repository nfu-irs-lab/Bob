package com.example.hiwin.teacher_version_bob.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.object.DataObject;
import com.example.hiwin.teacher_version_bob.object.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.view.FaceController;
import com.example.hiwin.teacher_version_bob.view.FaceFragment;
import com.example.hiwin.teacher_version_bob.view.FaceFragmentListener;
import com.example.hiwin.teacher_version_bob.view.ObjectShowerFragment;

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
                DataObject object = new DataObject("car", "車", "My car", "我的車");
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

    void showObjectAndFace(final DataObject object) throws InterruptedException {
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
