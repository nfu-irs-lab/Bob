package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;

public class NoSpeakFaceFragment extends Fragment {
    public enum Face {
        sad(R.raw.face_cry), happy(R.raw.face_happy);
        private final int id;

        Face(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private GifDrawable drawable;
    private FragmentListener listener;
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

    public void warp(Context context, Face face) throws IOException {
        drawable = new GifDrawable(context.getResources(), face.getId());
        drawable.setLoopCount(5);
        drawable.stop();
        drawable.addAnimationListener(i -> {
            if (listener != null && i + 1 == drawable.getLoopCount()) {
                listener.end();
            }
        });
    }

    private void start() {
        imageView.setImageDrawable(drawable);
        drawable.start();
        if (listener != null)
            listener.start();
    }

    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }

}
