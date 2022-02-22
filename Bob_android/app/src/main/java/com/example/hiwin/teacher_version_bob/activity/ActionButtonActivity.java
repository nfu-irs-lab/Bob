package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.R;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ActionButtonActivity extends BluetoothCommunicationActivity {

    private View action_button_frame;
    private ImageView face;

    @Override
    protected void receive(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8);
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.action_button_toolbar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_action_button;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        action_button_frame = findViewById(R.id.action_button_frame);
        face = findViewById(R.id.action_button_face);
    }

    @Override
    protected String getDeviceAddress(Bundle savedInstanceState) {
        Intent it = getIntent();
        return it.getStringExtra("address");
    }

    @Override
    protected void onConnect() {

    }

    @Override
    protected void onDisconnect() {

    }

    public void action(View view) {
        if (view.getId() == R.id.action_button_correct) {
            sendMessage("DO_ACTION correct.csv");
            action_button_frame.setVisibility(View.INVISIBLE);
        } else if (view.getId() == R.id.action_button_incorrect) {
            sendMessage("DO_ACTION incorrect.csv");
            action_button_frame.setVisibility(View.INVISIBLE);
        }
        boolean correct = view.getId() == R.id.action_button_correct;

        try {
            GifDrawable drawable = new GifDrawable(getResources(), correct ? R.drawable.face_gif_happy : R.drawable.face_gif_sad);
            face.setImageDrawable(drawable);
            drawable.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer.create(this, correct ? R.raw.sound_good_job: R.raw.sound_try_again).start();
        Handler handler = new Handler();
        new Thread(() -> {
            try {
                Thread.sleep(7000);
                handler.post(() -> {
                    action_button_frame.setVisibility(View.VISIBLE);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
