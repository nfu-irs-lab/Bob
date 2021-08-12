package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.R;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;

public class FaceFragment extends Fragment {

    public enum FaceType {
        car(R.raw.face_happy), cake(R.raw.face_happy), knife(R.raw.face_happy),
        bowl(R.raw.face_happy), person(R.raw.face_happy), bird(R.raw.face_happy),
        cat(R.raw.face_happy), bottle(R.raw.face_happy);
        private final int id;

        FaceType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private GifDrawable drawable;
    //    private GifController gifController;
    private FaceFragmentListener listener;
    private ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_face, container, false);
        imageView = (ImageView) root.findViewById(R.id.face_gif);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    public void warp(Context context, FaceType faceType) throws IOException {
        drawable = new GifDrawable(context.getResources(), faceType.getId());
        drawable.setLoopCount(10);
        drawable.addAnimationListener(i -> {
            if (listener != null)
                listener.complete(drawable,imageView);
        });
    }

    private void start() {
        imageView.setImageDrawable(drawable);
        drawable.start();
        if (listener != null)
            listener.start(drawable,imageView);
    }

    public void setListener(FaceFragmentListener listener) {
        this.listener = listener;
    }

}
