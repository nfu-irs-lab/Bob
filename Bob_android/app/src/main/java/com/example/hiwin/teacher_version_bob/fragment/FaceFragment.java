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

    public void warp(Context context, int face_gif_id, int loopCount, boolean endByAnimation) throws IOException {
        drawable = new GifDrawable(context.getResources(), face_gif_id);
        drawable.setLoopCount(loopCount);
        drawable.stop();
        if (endByAnimation) drawable.addAnimationListener(i -> {
            if (listener != null && i + 1 == drawable.getLoopCount()) {
                listener.end();
            }
        });
    }

    private void start() {
        imageView.setImageDrawable(drawable);
        drawable.start();
        if (listener != null) listener.start();
    }

    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }

}
