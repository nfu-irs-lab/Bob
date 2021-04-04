package com.example.hiwin.teacher_version_bob.view;

import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.view.anim.AnimateAction;

public class Face{
    private final ImageView left_eye;
    private final ImageView right_eye;
    private final ImageView mouth;
    private final View face;
    private final View root;
    public Face(View root) {
        this.root=root;
        this.face = root.findViewById(R.id.face_face);
        this.left_eye = (ImageView) face.findViewById(R.id.face_left_eye);
        this.right_eye = (ImageView) face.findViewById(R.id.face_right_eye);
        this.mouth = (ImageView) face.findViewById(R.id.face_mouth);
    }

    public void doAnimation(AnimateAction animateAction, int repeatCount){
        AnimatorSet set=animateAction.getAnimatorSet(this,repeatCount);
        set.start();
    }

    public void showFace() {
        left_eye.setVisibility(View.VISIBLE);
        right_eye.setVisibility(View.VISIBLE);
        mouth.setVisibility(View.VISIBLE);
    }

    public void hindFace() {
        left_eye.setVisibility(View.INVISIBLE);
        right_eye.setVisibility(View.INVISIBLE);
        mouth.setVisibility(View.INVISIBLE);
    }

    public ImageView getLeftEye() {
        return left_eye;
    }
    public ImageView getRightEye() {
        return right_eye;
    }
    public ImageView getMouth() {
        return left_eye;
    }

    public View getFace() {
        return face;
    }

    public View getRoot() {
        return root;
    }
}
