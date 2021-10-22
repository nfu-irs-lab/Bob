package com.example.hiwin.teacher_version_bob.activity;

import android.os.*;
import android.util.Base64;
import android.util.Log;
import com.example.hiwin.teacher_version_bob.data.DataSpeaker;
import com.example.hiwin.teacher_version_bob.data.concrete.object.parser.JSONDataParser;
import com.example.hiwin.teacher_version_bob.data.concrete.pack.Base64Package;
import com.example.hiwin.teacher_version_bob.data.data.Data;
import com.example.hiwin.teacher_version_bob.fragment.FragmentListener;
import com.example.hiwin.teacher_version_bob.fragment.FaceFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FaceDetectActivity extends DetectActivity {
    private static final String THIS_LOG_TAG = "FaceDetectActivity";
    private DataSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speaker = new DataSpeaker(this);
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String content = new String(new Base64Package(data, Base64.DEFAULT).getDecoded(), StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, content);

            FaceFragment faceFragment = new FaceFragment();
            faceFragment.setListener(new FragmentListener() {
                @Override
                public void start() {

                }

                @Override
                public void end() {
                    showDefault();
                }
            });
            Data face = new JSONDataParser("zh_TW").parse(new JSONObject(content));
            faceFragment.warp(this, FaceFragment.Face.valueOf(face.getName()), 5, true);
            speaker.speakFully(face);
            postFragment(faceFragment, "face");
            detect_pause();
        } catch (IllegalArgumentException | IOException | JSONException e) {
            Log.d(THIS_LOG_TAG, "base64 decode error:");
            Log.d(THIS_LOG_TAG, e.getMessage());
        }

    }
}
