package com.example.hiwin.teacher_version_bob.view;

import android.content.res.Resources;
import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.R;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;

public class FaceController {
    public enum FaceType {
        car(R.raw.face_happy), cake(R.raw.git_cake), knife(R.raw.gif_knife);
        private final int id;

        FaceType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public interface FaceListener {
        void onFaceMotionStarted(FaceController controller);
        void onFaceMotionComplete(FaceController controller);

    }

    private final GifController gifController;
    private final Resources resources;
    private FaceListener listener;

    public FaceController(ImageView imageView, Resources resources) {
        this.gifController = new GifController(imageView);
        this.resources = resources;
    }

    public void warp(FaceType faceType) throws IOException {
        final FaceController f = this;
        GifDrawable drawable = new GifDrawable(resources, faceType.getId());
        drawable.setLoopCount(10);
        drawable.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationCompleted(int i) {
                listener.onFaceMotionComplete(f);
            }
        });

        gifController.warpDrawable(drawable);
    }

    public void start() {
        listener.onFaceMotionStarted(this);
        gifController.start();
    }

    public void stop() {
        gifController.stop();
    }

    public void show() {
        gifController.show();
    }

    public void hind() {
        gifController.hind();
    }

    public void setListener(FaceListener listener) {
        this.listener = listener;
    }
}
