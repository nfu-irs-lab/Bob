package com.example.hiwin.teacher_version_bob.activity;

import android.content.Context;
import android.os.*;
import android.util.Base64;
import android.util.Log;
import com.example.hiwin.teacher_version_bob.data.concrete.object.parser.JSONDataParser;
import com.example.hiwin.teacher_version_bob.data.concrete.pack.Base64Package;
import com.example.hiwin.teacher_version_bob.data.data.Data;
import com.example.hiwin.teacher_version_bob.handler.ReceiveFragmentHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class ObjectDetectActivity extends DetectActivity {
    private static final String THIS_LOG_TAG = "MainActivity";
    private ReceiveFragmentHandler handler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        handler = new ReceiveFragmentHandler(context, Looper.getMainLooper(), getSupportFragmentManager()) {
            @Override
            protected void onComplete() {
                detect_pause();
                showDefault();
            }
        };
    }

    private void showObjectAndFace(final Data object) {
        Message msg = new Message();
        msg.what = ReceiveFragmentHandler.CODE_RECEIVE;
        msg.obj = object;
        handler.sendMessage(msg);
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String content = new String(new Base64Package(data, Base64.DEFAULT).getDecoded(), StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, content);

            try {
                detect_pause();
                showObjectAndFace((new JSONDataParser("zh_TW")).parse(new JSONObject(content)));
            } catch (JSONException e) {
                Log.e(THIS_LOG_TAG, e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            Log.d(THIS_LOG_TAG, "base64 decode error:");
            Log.d(THIS_LOG_TAG, e.getMessage());
        }

    }
}
