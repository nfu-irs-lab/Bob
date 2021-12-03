package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialListener;
import com.example.hiwin.teacher_version_bob.fragment.ObjectSelectFragment;

import java.nio.charset.StandardCharsets;

public class InteractiveObjectDetectActivity extends BluetoothCommunicationActivity {

    private static final String THIS_LOG_TAG = "InteractiveObjectDetectActivity ";

    @Override
    protected void initialize(Bundle savedInstanceState) {
        postFragment(new ObjectSelectFragment(), "object_select");
    }

    @Override
    protected String getDeviceAddress(Bundle savedInstanceState) {
        Intent it = getIntent();
        return it.getStringExtra("address");
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String content = new String(data, StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, content);

        } catch (IllegalArgumentException e) {
            Log.d(THIS_LOG_TAG, e.getMessage());
        }
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.inter_obj_detect_toolbar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_inter_obj_detect;
    }

    private void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.inter_obj_detect_frame, fragment, id);
        fragmentTransaction.commit();
    }


    @Override
    protected SerialListener getListener() {
        return new SerialListener() {
            @Override
            public void onSerialConnect() {
                sendMessage("DB_GET_ALL");
            }

            @Override
            public void onSerialConnectError(Exception e) {

            }

            @Override
            public void onSerialRead(byte[] data) {

            }

            @Override
            public void onSerialIoError(Exception e) {

            }
        };
    }

}
