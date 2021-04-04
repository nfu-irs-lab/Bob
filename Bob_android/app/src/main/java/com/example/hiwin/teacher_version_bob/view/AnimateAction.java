package com.example.hiwin.teacher_version_bob.view;

import android.animation.Animator;

public interface AnimateAction {
    public void start();
    public void attach(Animator.AnimatorListener listener);

}
