package com.example.hiwin.teacher_version_bob.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;
import com.example.hiwin.teacher_version_bob.fragment.FaceFragment;
import com.example.hiwin.teacher_version_bob.fragment.FaceFragmentListener;
import com.example.hiwin.teacher_version_bob.fragment.ObjectShowerFragment;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;

public class ReceiveFragmentHandler extends Handler {


    public static final int CODE_FACE = 1;
    public static final int CODE_OBJECT = 2;

    public static final Message MSG_FACE;
    public static final Message MSG_OBJECT;

    static {
        MSG_FACE = new Message();
        MSG_FACE.what = CODE_FACE;

        MSG_OBJECT = new Message();
        MSG_OBJECT.what = CODE_OBJECT;
    }

    private final FragmentManager fragmentManager;
    private final Context context;
    private final ObjectSpeaker speaker;
    private boolean isOperating = false;

    public ReceiveFragmentHandler(Context context, Looper looper, FragmentManager fragmentManager) {
        super(looper);
        speaker = new ObjectSpeaker(context);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        DataObject object = (DataObject) msg.obj;

        switch (msg.what) {
            case CODE_FACE:
                final FaceFragment faceFragment = new FaceFragment();
                try {
                    faceFragment.warp(context, FaceFragment.FaceType.valueOf(object.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                faceFragment.setListener(new FaceFragmentListener() {
                    @Override
                    public void start(GifDrawable drawable, ImageView imageView) {
                        speaker.setSpeakerListener(() -> {
                            post(()->imageView.setVisibility(View.INVISIBLE));
                            synchronized (this) {
                                isOperating = false;
                            }
                        });
                        speaker.speak(object);
                    }

                    @Override
                    public void complete(GifDrawable drawable, ImageView imageView) {

                    }

                });

                postFragment(faceFragment, "face2");
                break;
            case CODE_OBJECT:
                synchronized (this) {
                    isOperating = true;
                }
                final ObjectShowerFragment objectShowerFragment = new ObjectShowerFragment();
                objectShowerFragment.warp(context, object);
                postFragment(objectShowerFragment, "shower");
                break;
        }
        super.handleMessage(msg);
    }

    public void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frame, fragment, id);
        fragmentTransaction.commit();
    }

    public boolean isOperating() {
        return isOperating;
    }
}
