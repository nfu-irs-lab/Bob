package com.example.hiwin.teacher_version_bob.view.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.example.hiwin.teacher_version_bob.view.Face;

public class SeeLeftAndRightAction implements AnimateAction, Animator.AnimatorListener {
    private Animator.AnimatorListener listener;

    public SeeLeftAndRightAction(Animator.AnimatorListener listener) {
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
                face.getFace(), "translationX", 0f, 200f, 0f, -200f, 0);
        int r = (int) ((Math.random() * 12000) + 5000);
        animation.setRepeatCount(repeatCount);

        animation.setDuration(r);
        bouncer.play(animation);

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
