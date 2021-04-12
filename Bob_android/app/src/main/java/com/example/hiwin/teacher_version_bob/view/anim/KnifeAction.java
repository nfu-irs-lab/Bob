package com.example.hiwin.teacher_version_bob.view.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.view.Face;

public class KnifeAction implements AnimateAction, Animator.AnimatorListener {
    private Animator.AnimatorListener listener;
    private Context context;
    public KnifeAction(Context context, Animator.AnimatorListener listener) {
        this.listener = listener;
        this.context=context;
    }

    public void attach(Animator.AnimatorListener listener) {
        synchronized (this) {
            this.listener = listener;
        }
    }

    public AnimatorSet getAnimatorSet(Face face, int repeatCount) {
        face.getIface().setImageDrawable(context.getDrawable(R.drawable.face_grimacing));
        final AnimatorSet bouncer = new AnimatorSet();
        bouncer.addListener(this);
        ObjectAnimator animation = ObjectAnimator.ofFloat(
                face.getIface(), "translationX", 0f, 0f, 0f, 0f, 0f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(
                face.getIface(), "translationX", 0f, 0f, 0f, 0f, 0f);
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
