package com.example.hiwin.teacher_version_bob.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;

public class SeeLeftAndRightAction implements AnimateAction, Animator.AnimatorListener {
    private final Activity activity;
    private final Face face;
    private Animator.AnimatorListener listener;

    public SeeLeftAndRightAction(Activity activity, Face face) {
        this.activity = activity;
        this.face = face;
    }

    public void attach(Animator.AnimatorListener listener) {
        this.listener = listener;
    }


    @Override
    public void start() {
        face.showFace();
        final AnimatorSet bouncer = new AnimatorSet();
        bouncer.addListener(this);
        if (!bouncer.isRunning()) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(
                    face.getFace(), "translationX", 0f, 200f, 0f, -200f, 0);
            int r = (int) ((Math.random() * 12000) + 5000);
            animation.setDuration(r);
            bouncer.play(animation);
        }


        bouncer.start();

    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (listener != null)
            listener.onAnimationStart(animation);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (listener != null)
            listener.onAnimationEnd(animation);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if (listener != null)
            listener.onAnimationCancel(animation);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if (listener != null)
            listener.onAnimationRepeat(animation);
    }
}
