package com.example.hiwin.teacher_version_bob;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.hiwin.teacher_version_bob.view.anim.AnimateAction;
import com.example.hiwin.teacher_version_bob.view.FaceFragment;
import com.example.hiwin.teacher_version_bob.view.ObjectShowerFragment;
import com.example.hiwin.teacher_version_bob.view.anim.CarAction;
import com.example.hiwin.teacher_version_bob.view.anim.SeeLeftAndRightAction;

import java.util.HashMap;
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


    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main_root = (ConstraintLayout) findViewById(R.id.main_root);
        fragmentManager = getSupportFragmentManager();

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

        AnimateAction action = new SeeLeftAndRightAction(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Toast.makeText(context, "repeat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
//                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
//                Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
            }



        });
        FaceFragment fragment = new FaceFragment(action, 0);
        postFragment(fragment, "face");

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        AnimateAction action=new SeeLeftAndRightAction();
//        FaceFragment fragment = new FaceFragment(action, Animation.INFINITE);
//
//        fragmentTransaction.replace(R.id.frame, fragment, "face");
//        fragmentTransaction.commit();

//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View face_root = inflater.inflate(R.layout.activity_content_face, (ViewGroup) findViewById(R.id.face_root));
//
//        final Face face = new Face(face_root);
//        contentView=face.getRoot();
//        ConstraintSet set = new ConstraintSet();
//        contentView.setId(View.generateViewId());
//        ViewGroup.LayoutParams parms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        contentView.setLayoutParams(parms);
//
//        main_root.addView(contentView, 0);
//        set.clone(main_root);
//        set.connect(contentView.getId(), ConstraintSet.TOP, R.id.main_toolbar, ConstraintSet.BOTTOM, 0);
//        set.connect(contentView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
//        set.applyTo(main_root);
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final AnimateAction action = new SeeLeftAndRightAction(MainActivity2.this, face);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (this) {
//                            action.start();
//                        }
//                    }
//                });
//                while (true) {
//                    action.attach(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    synchronized (this) {
//                                        action.start();
//                                    }
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            super.onAnimationStart(animation);
//                        }
//                    });
//
//                }
//            }
//       new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final AnimateAction action = new SeeLeftAndRightAction(MainActivity2.this, face);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (this) {
//                            action.start();
//                        }
//                    }
//                });
//                while (true) {
//                    action.attach(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    synchronized (this) {
//                                        action.start();
//                                    }
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            super.onAnimationStart(animation);
//                        }
//                    });
//
//                }
//            }
//        }).start();

    }

    public void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frame, fragment, id);
        fragmentTransaction.commit();
    }

    public void removeFragment(String id) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


    public void test(View v) {
        showObjectAndFace("car", "車", "My car is beautiful.", "我的車很猛");
    }

    void showObjectAndFace(final String name, final String tr_name, final String sentence, final String tr_sentence) {

//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View face_root = inflater.inflate(R.layout.activity_content_face, (ViewGroup) findViewById(R.id.face_root));
//        View object_root = inflater.inflate(R.layout.activity_content_object, (ViewGroup) findViewById(R.id.object_root));

//
//        FragmentManager fragmentManager =getSupportFragmentManager();
//        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        FaceFragment fragment = new FaceFragment();
//        fragmentTransaction.replace(R.id.frame, fragment, "face");



        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,Object> data=new HashMap<>();
                        data.put("img",getDrawableByString(name));
                        data.put("name",(name));
                        data.put("tr_name",(tr_name));
                        ObjectShowerFragment fragment=new ObjectShowerFragment(data);
                        postFragment(fragment,"shower");
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
//                        removeFragment("face");

                        AnimateAction action=new CarAction(new AnimatorListenerAdapter() {

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
//                                face.hindFace();
                            }

                        });

                        FaceFragment fragment = new FaceFragment(action, 1);
                        postFragment(fragment, "face2");

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
