package com.example.hiwin.teacher_version_bob;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hiwin.teacher_version_bob.view.AnimateAction;
import com.example.hiwin.teacher_version_bob.view.Face;
import com.example.hiwin.teacher_version_bob.view.ObjectShower;
import com.example.hiwin.teacher_version_bob.view.SeeLeftAndRightAction;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    /*
        reference:
            https://developer.android.com/guide/topics/connectivity/bluetooth.html
            https://xnfood.com.tw/activity-life-cycle/
            https://zh.wikipedia.org/wiki/%E8%97%8D%E7%89%99%E8%A6%8F%E7%AF%84#%E5%BA%8F%E5%88%97%E5%9F%A0%E8%A6%8F%E7%AF%84_%EF%BC%88SPP%EF%BC%89
            https://developer.android.com/guide/components/activities/activity-lifecycle
            https://developer.android.com/guide/components/fragments
            https://developer.android.com/guide/components/services
            https://www.tutorialspoint.com/android/android_text_to_speech.htm
            https://medium.com/verybuy-dev/android-%E8%A3%A1%E7%9A%84%E7%B4%84%E6%9D%9F%E6%80%96%E5%B1%80-constraintlayout-6225227945ab
            https://materialdesignicons.com/
     */

    private Context context;
    private TextToSpeech textToSpeech;

    private ConstraintLayout main_root;
    private View contentView;


    private Face face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main_root = (ConstraintLayout) findViewById(R.id.main_root);
        setSupportActionBar(toolbar);
        context = this;

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View face_root = inflater.inflate(R.layout.activity_content_face, (ViewGroup) findViewById(R.id.face_root));

        final Face face = new Face(face_root);
        contentView=face.getRoot();
        ConstraintSet set = new ConstraintSet();
        contentView.setId(View.generateViewId());
        ViewGroup.LayoutParams parms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(parms);

        main_root.addView(contentView, 0);
        set.clone(main_root);
        set.connect(contentView.getId(), ConstraintSet.TOP, R.id.main_toolbar, ConstraintSet.BOTTOM, 0);
        set.connect(contentView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.applyTo(main_root);



        new Thread(new Runnable() {
            @Override
            public void run() {
                final AnimateAction action = new SeeLeftAndRightAction(MainActivity2.this, face);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            action.start();
                        }
                    }
                });
                while (true) {
                    action.attach(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        action.start();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                        }
                    });

                }
            }
        }).start();

    }


    public void test(View v) {
        showObjectAndFace("car", "車", "My car is beautiful.", "我的車很猛");
    }

    void showObjectAndFace(final String name, final String tr_name, final String sentence, final String tr_sentence) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View face_root = inflater.inflate(R.layout.activity_content_face, (ViewGroup) findViewById(R.id.face_root));
        View object_root = inflater.inflate(R.layout.activity_content_object, (ViewGroup) findViewById(R.id.object_root));

        final Face face = new Face(face_root);
        final ObjectShower objectShower = new ObjectShower(object_root);


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentView = objectShower.getRoot();
                        objectShower.setData(getDrawableByString(name), name, tr_name);
                        objectShower.show();
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentView = face.getRoot();
                        face.showFace();
                        AnimateAction action = new SeeLeftAndRightAction(MainActivity2.this, face);
                        action.attach(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                if (!textToSpeech.isSpeaking()) {
                                    textToSpeech.setLanguage(Locale.US);
                                    textToSpeech.speak(name, TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak(name, TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak(name, TextToSpeech.QUEUE_ADD, null);

                                    textToSpeech.setLanguage(Locale.TAIWAN);
                                    textToSpeech.speak(tr_name, TextToSpeech.QUEUE_ADD, null);

                                    textToSpeech.setLanguage(Locale.US);
                                    textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.setLanguage(Locale.TAIWAN);
                                    textToSpeech.speak(tr_sentence, TextToSpeech.QUEUE_ADD, null);
                                }
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Toast.makeText(MainActivity2.this, "end", Toast.LENGTH_LONG).show();
                                face.hindFace();
                            }

                        });

                        action.start();
                    }
                });


            }
        }).start();

    }


    Drawable getDrawableByString(String str) {
        switch (str) {
            case "car":

                return getDrawable(R.drawable.ic_eco_car);

            case "knife":
                return getDrawable(R.drawable.ic_french_knife);

            case "cake":
                return getDrawable(R.drawable.ic_birthday_cake);
        }
        return null;
    }


}
