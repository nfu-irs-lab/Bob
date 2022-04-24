package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class InteractiveObjectDetectActivity extends BluetoothCommunicationActivity {


    private static final String THIS_LOG_TAG = "InteractiveObjectDetectActivity";
    private final LinkedList<JSONObject> available_vocabulary = new LinkedList<>();
    private JSONArray objects;
    private String answer;
    private final Handler handler = new Handler();
    private Fragment currentQuestion;

    @Override
    protected void initialize(Bundle savedInstanceState) {
    }

    @Override
    protected String getDeviceAddress(Bundle savedInstanceState) {
        Intent it = getIntent();
        return it.getStringExtra("address");
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String str = new String(data, StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, str);
            JSONObject json = new JSONObject(str);
            String content = json.getString("content");
            if (content.equals("all_objects")) {
                JSONArray raw = json.getJSONArray("data");
                objects = new JSONArray();
                for (int i = 0; i < 9; i++) {
                    objects.put(raw.getJSONObject(i));
                }

                reset();
                selectNewAnswer();
                sendMessage("DETECT_INTER_OBJECT");
                sendMessage("START_DETECT");
            } else if (content.equals("single_object")) {
                String detected_object = json.getJSONObject("data").getString("name");

                AnswerFragment fragment = new AnswerFragment();
                boolean correct = detected_object.equals(answer);
                postFragment(fragment, "A");
                new Thread(() -> {

                    if (correct) {
                        fragment.correct();
                    } else {
                        fragment.incorrect();
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> {
                        try {
                            if (correct)
                                selectNewAnswer();
                            else
                                postFragment(currentQuestion, "UU");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }).start();
            } else {
                throw new IllegalStateException("Unknown state");
            }


        } catch (IllegalArgumentException | JSONException e) {
            Log.e(THIS_LOG_TAG, e.getMessage());
        }
    }

    private void reset() throws JSONException {
        available_vocabulary.clear();
        for (int i = 0; i < objects.length(); i++) {
            available_vocabulary.add(objects.getJSONObject(i));
        }
    }

    private void selectNewAnswer() throws JSONException {
        if (available_vocabulary.size() == 0)
            reset();

        int i = (int) (Math.random() * available_vocabulary.size());
        JSONObject selected = available_vocabulary.get(i).getJSONObject("data");

        answer = selected.getString("name");
        String definition = selected.getString("definition");
        currentQuestion = getEntryDetectFragment(definition);
        postFragment(currentQuestion, "AA");
        available_vocabulary.remove(i);
    }


//    public interface DetectedListener {
//        void onDetected(String obj);
//    }

    private Fragment getEntryDetectFragment(String definition) {
        EntryObjectDetectFragment objectDetectFragment = new EntryObjectDetectFragment();
        objectDetectFragment.setDefinition(definition);
        return objectDetectFragment;
    }


    @Override
    protected void onConnect() {
        sendMessage("DB_GET_ALL");
    }

    @Override
    protected void onDisconnect() {
//        sendMessage("PAUSE_DETECT");
        sendMessage("STOP_DETECT");
    }

    @Override
    public void onStop() {
//        sendMessage("PAUSE_DETECT");
        if (isConnected())
            sendMessage("STOP_DETECT");
        super.onStop();
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


}
