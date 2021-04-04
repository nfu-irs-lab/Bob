package com.example.hiwin.teacher_version_bob.view.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;

import com.example.hiwin.teacher_version_bob.view.Face;

public interface AnimateAction {
    public void attach(Animator.AnimatorListener listener);
    public AnimatorSet getAnimatorSet(Face face, int repeatCount);

}
