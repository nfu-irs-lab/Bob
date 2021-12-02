package com.example.hiwin.teacher_version_bob.activity;

import android.app.Service;
import android.content.Context;
import android.os.*;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.DataSpeaker;
import com.example.hiwin.teacher_version_bob.data.concrete.object.parser.JSONDataParser;
import com.example.hiwin.teacher_version_bob.data.concrete.pack.Base64Package;
import com.example.hiwin.teacher_version_bob.data.data.Data;
import com.example.hiwin.teacher_version_bob.data.data.Face;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FaceDetectActivity extends DetectActivity {
    private static final String THIS_LOG_TAG = "FaceDetectActivity";
    private DataSpeaker speaker;
    private Context context;

    @Override
    protected void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        context = this;

        speaker = new DataSpeaker(new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {

                Log.d(THIS_LOG_TAG, "TextToSpeech is initialized");
                Toast.makeText(context, "TextToSpeech is initialized", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(THIS_LOG_TAG, "TextToSpeech initializing error");
                Toast.makeText(context, "TextToSpeech initializing error", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void onComplete() {
        showDefault();
    }

    @Override
    protected void showDefault() {
        final DefaultFragment fragment = new DefaultFragment();
        fragment.setListener(new FragmentListener() {
            @Override
            public void start() {
            }

            @Override
            public void end() {
                Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                myVibrator.vibrate(100);
                if (isConnected())
                    detect_start();
                else {
                    Toast.makeText(FaceDetectActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        postFragment(fragment, "default");
    }

    private void showFace(final Data object) throws IOException {
        Fragment finalFaceFragment = getFinalFaceFragment(object.getFace(), null, "null");
        Fragment exampleFragment = getExampleFragment(object, finalFaceFragment, "face2");
        Fragment faceFragment = getFaceFragment(object, exampleFragment, "example");
        Fragment descriptionFragment = getDescriptionFragment(object, faceFragment, "face");
        postFragment(descriptionFragment, "description");
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String content = new String(new Base64Package(data, Base64.DEFAULT).getDecoded(), StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, content);

            try {
                detect_pause();
                showFace((new JSONDataParser("zh_TW")).parse(new JSONObject(content)));
            } catch (Exception e) {
                Log.e(THIS_LOG_TAG, e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            Log.d(THIS_LOG_TAG, e.getMessage());
        }

    }


    private Fragment getFinalFaceFragment(Face face, Fragment next, String nextId) throws IOException {
        FaceFragment faceFragment = new FaceFragment();
        faceFragment.warp(context, face, 2, true);
        faceFragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            protected void postFragment(Fragment next, String nextId) {
                FaceDetectActivity.this.postFragment(next, nextId);
            }

            @Override
            public void end() {
                super.end();
                onComplete();
            }
        });
        return faceFragment;
    }

    private Fragment getFaceFragment(Data object, Fragment next, String nextId) throws IOException {

        FaceFragment faceFragment = new FaceFragment();
        faceFragment.warp(context, object.getFace(), 5, false);

        faceFragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            protected void postFragment(Fragment next, String nextId) {
                FaceDetectActivity.this.postFragment(next, nextId);
            }

            @Override
            public void start() {
                super.start();
                speaker.setSpeakerListener(this::end);
                speaker.speakFully(object);
            }

            @Override
            public void end() {
                super.end();
            }
        });

        return faceFragment;
    }

    public Fragment getDescriptionFragment(Data data, Fragment next, String nextId) {
        final DescriptionFragment descriptionFragment = new DescriptionFragment();
        descriptionFragment.setShowListener((views) -> {
            ((ImageView) views[0]).setImageDrawable(context.getDrawable(data.getFace().getGifId()));
            ((TextView) views[1]).setText(data.getName());
            ((TextView) views[2]).setText(data.getTranslatedName());
        });

        descriptionFragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            public void start() {
                super.start();
                new Thread(() -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    end();
                }).start();
            }

            @Override
            protected void postFragment(Fragment next, String nextId) {
                FaceDetectActivity.this.postFragment(next, nextId);
            }
        });

        return descriptionFragment;
    }

    private Fragment getExampleFragment(Data object, Fragment next, String nextId) {
        final ExampleFragment fragment = new ExampleFragment();
        fragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            protected void postFragment(Fragment next, String nextId) {
                FaceDetectActivity.this.postFragment(next, nextId);
            }

            @Override
            public void start() {
                super.start();
                speaker.speakExample(object);
                speaker.setSpeakerListener(this::end);
            }
        });

        fragment.setShowListener(views -> {
            ((TextView) views[0]).setText(object.getSentence());
            ((TextView) views[1]).setText(object.getTranslatedSentence());
        });
        return fragment;

    }



    @Override
    public void onStop() {
        super.onStop();
        speaker.shutdown();
    }

}
