package com.example.hiwin.teacher_version_bob.view;

import android.view.View;
import android.widget.ImageView;
import pl.droidsonroids.gif.GifDrawable;

public class GifController {
    private final ImageView imageView;
    private GifDrawable drawable;

    public GifController(ImageView imageView) {
        this.imageView = imageView;
    }
    public void warpDrawable(GifDrawable drawable){
        this.drawable=drawable;
        imageView.setImageDrawable(drawable);
    }

    public void start(GifController gifController){
        drawable.start();
    }

    public void stop() {
        drawable.stop();
    }

    public void show() {
        imageView.setVisibility(View.VISIBLE);
    }

    public void hind() {
        imageView.setVisibility(View.INVISIBLE);
    }


}