package com.example.hiwin.teacher_version_bob.view;

import android.view.View;
import android.widget.ImageView;

import com.example.hiwin.teacher_version_bob.R;

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
//
//
//    private AnimatorSet getSeeLeftAndRight(Animator.AnimatorListener listener) {
//
//        AnimatorSet bouncer = new AnimatorSet();
//        bouncer.addListener(listener);
//
//        if (!bouncer.isRunning()) {
//            ObjectAnimator animation = ObjectAnimator.ofFloat(left_eye, "translationX", 0f, 200f, 0f, -200f, 0);
//            ObjectAnimator animation2 = ObjectAnimator.ofFloat(right_eye, "translationX", 0f, 200f, 0f, -200f, 0);
//            ObjectAnimator animation3 = ObjectAnimator.ofFloat(mouth, "translationX", 0f, 220f, 0f, -220f, 0);
//            int r = (int) ((Math.random() * 12000) + 5000);
//            animation.setDuration(r);
//            animation2.setDuration(r);
//            animation3.setDuration(r);
//            bouncer.play(animation).with(animation2).with(animation3);
//        }
//        return bouncer;
//    }

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
