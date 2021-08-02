package com.example.hiwin.teacher_version_bob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.object.JObject;
import com.example.hiwin.teacher_version_bob.object.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.view.FaceController;


public class MainActivity2 extends AppCompatActivity {
    ObjectSpeaker speaker;
    FaceController faceController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        faceController=new FaceController((ImageView)findViewById(R.id.sssss),getResources());
        faceController.setListener(new FaceController.FaceListener() {
            @Override
            public void onFaceMotionStarted(FaceController controller) {
                JObject object=new JObject("Car","車","My car","我的車");
                speaker.speak(object);
            }

            @Override
            public void onFaceMotionComplete(FaceController controller) {
                Toast.makeText(MainActivity2.this,"Gif Complete",Toast.LENGTH_SHORT).show();
//                controller.hind();
            }
        });

        speaker=new ObjectSpeaker(context);
        speaker.setSpeakerListener(new ObjectSpeaker.SpeakerListener() {
            @Override
            public void onSpeakComplete() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity2.this,"Speaker Complete",Toast.LENGTH_SHORT).show();

                        faceController.hind();
                    }
                });
            }
        });

        try {
            faceController.warp(FaceController.FaceType.car);
            faceController.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean s=false;
    @SuppressLint("NonConstantResourceId")
    public void startGif(View v) {
        s=!s;
        if(s)
            faceController.start();
        else
            faceController.stop();
        try {
            switch (v.getId()) {
                case R.id.btn_gif_1:
                    faceController.warp(FaceController.FaceType.car);
                    break;

                case R.id.btn_gif_2:
                    faceController.warp(FaceController.FaceType.cake);
                    break;

                case R.id.btn_gif_3:
                    faceController.warp(FaceController.FaceType.knife);
                    break;
            }
            faceController.show();
            faceController.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}