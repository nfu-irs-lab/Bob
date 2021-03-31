package com.example.hiwin.teacher_version_bob;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FaceAdaptor implements Runnable {
    private final ImageView left_eye;
    private final ImageView right_eye;
    private final ImageView mouth;

    public FaceAdaptor(ImageView left_eye, ImageView right_eye, ImageView mouth) {
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.mouth = mouth;
    }


    @Override
    public void run() {
        AnimatorSet bouncer=null;

            if(bouncer==null||!bouncer.isRunning()){
                bouncer = new AnimatorSet();
                ObjectAnimator animation = ObjectAnimator.ofFloat(left_eye, "translationX", 0f, 400f,0f, -400f, 0);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(right_eye, "translationX", 0f, 400f,0f, -400f, 0);
                ObjectAnimator animation3 = ObjectAnimator.ofFloat(mouth, "translationX", 0f, 400f,0f, -400f, 0);
                animation.setDuration(7000);
                animation2.setDuration(7000);
                animation3.setDuration(7000);
                bouncer.play(animation).with(animation2).with(animation3);
                bouncer.start();
            }
    }
}
