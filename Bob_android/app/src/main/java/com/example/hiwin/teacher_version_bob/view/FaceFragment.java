package com.example.hiwin.teacher_version_bob.view;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.io.IOException;

public class FaceFragment extends Fragment implements FaceFragmentListener {
    private Face face;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FaceFragmentListener listener;

    public void setListener(FaceFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_content_face, container, false);
        try {
            face = new Face(getResources(), Face.FaceType.getInstance(getArguments().getString("face_type")), root);
            face.hindFace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int duration = getArguments().getInt("duration");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                face.showFace();

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeout();
            }
        }).start();

    }

    public Face getFace() {
        return face;
    }

    @Override
    public void start() {
        listener.start();
    }

    @Override
    public void timeout() {
        listener.timeout();
    }
}
