package com.example.hiwin.teacher_version_bob.view.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.view.Face;

public class CarAction implements AnimateAction, Animator.AnimatorListener {
    private Animator.AnimatorListener listener;
    private Context context;
    public CarAction(Context context, Animator.AnimatorListener listener) {
        this.listener = listener;
        this.context=context;
    }

    public void attach(Animator.AnimatorListener listener) {
        synchronized (this) {
            this.listener = listener;
        }
    }

    public AnimatorSet getAnimatorSet(Face face, int repeatCount) {
        face.getLeftEye().setImageDrawable(context.getDrawable(R.drawable.ic_eye));
        face.getRightEye().setImageDrawable(context.getDrawable(R.drawable.ic_eye));
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
