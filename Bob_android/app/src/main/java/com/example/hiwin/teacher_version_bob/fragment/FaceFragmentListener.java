package com.example.hiwin.teacher_version_bob.fragment;

import android.widget.ImageView;
import pl.droidsonroids.gif.GifDrawable;

public interface FaceFragmentListener {
   void start(GifDrawable drawable, ImageView imageView);
   void complete(GifDrawable drawable, ImageView imageView);

}
