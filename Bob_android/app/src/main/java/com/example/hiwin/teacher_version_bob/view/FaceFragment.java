package com.example.hiwin.teacher_version_bob.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.view.anim.AnimateAction;

@SuppressLint("ValidFragment")
public class FaceFragment extends Fragment {
    private Face face;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final AnimateAction animateAction;
    private final int repeatCount;
    public FaceFragment(AnimateAction animateAction,int repeatCount){
        this.animateAction=animateAction;
        this.repeatCount=repeatCount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_content_face, container, false);
        face = new Face(root);
        face.hindFace();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                face.showFace();
                face.doAnimation(animateAction,repeatCount);
            }
        });

    }

    public Face getFace() {
        return face;
    }

}
