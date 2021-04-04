package com.example.hiwin.teacher_version_bob.view.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.example.hiwin.teacher_version_bob.view.Face;

public class CarAction implements AnimateAction, Animator.AnimatorListener {
    private Animator.AnimatorListener listener;

    public CarAction(Animator.AnimatorListener listener) {
        this.listener = listener;
    }

    public void attach(Animator.AnimatorListener listener) {
        synchronized (this) {
            this.listener = listener;
        }
    }

    public AnimatorSet getAnimatorSet(Face face, int repeatCount) {
        final AnimatorSet bouncer = new AnimatorSet();
        bouncer.addListener(this);
        ObjectAnimator animation = ObjectAnimator.ofFloat(
                face.getLeftEye(), "translationX", 0f, 200f, 0f, -200f, 0);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(
                face.getRightEye(), "translationX", 0f, 200f, 0f, -200f, 0);
        int r =5000;
        animation.setRepeatCount(repeatCount);
        animation2.setRepeatCount(repeatCount);

        animation.setDuration(r);
        animation2.setDuration(r);
        bouncer.play(animation).with(animation2);
        return bouncer;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        synchronized (this) {
            if (listener != null)
                listener.onAnimationStart(animation);
        }
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
