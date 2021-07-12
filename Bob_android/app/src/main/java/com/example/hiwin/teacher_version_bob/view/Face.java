package com.example.hiwin.teacher_version_bob.view;

import android.animation.AnimatorSet;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import com.example.hiwin.teacher_version_bob.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class Face {
    public static enum FaceType {
        car(R.raw.test);

        private int resource;

        FaceType(int resource) {
            this.resource = resource;
        }

        public static FaceType getInstance(String str) {
            FaceType[] faceTypes = values();

            for (FaceType faceType : faceTypes) {
                if (str.equals(faceType.toString())) {
                    return faceType;
                }
            }
            throw new RuntimeException();
        }

        public int getResource() {
            return resource;
        }
    }

    private final ImageView iface;
    private final View root;
    private final Resources resources;
    private GifDrawable gifFromResource;

    public Face(Resources resources, FaceType faceType, View root) throws IOException {
        this.root = root;
        this.iface = (ImageView) root.findViewById(R.id.face_gif);
        this.resources = resources;
        gifFromResource = new GifDrawable(resources, faceType.resource);
        gifFromResource.start();
        iface.setImageDrawable(gifFromResource);
    }

    public void stopFace(){
        gifFromResource.stop();
    }

    public void showFace() {
        iface.setVisibility(View.VISIBLE);
    }

    public void hindFace() {
        iface.setVisibility(View.INVISIBLE);
    }


    public View getRoot() {
        return root;
    }
}
