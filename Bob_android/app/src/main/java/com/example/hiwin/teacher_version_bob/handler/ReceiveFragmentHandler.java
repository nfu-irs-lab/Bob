package com.example.hiwin.teacher_version_bob.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;
import com.example.hiwin.teacher_version_bob.fragment.*;

import java.io.IOException;

public class ReceiveFragmentHandler extends Handler {


    public static final int CODE_FACE = 1;
    public static final int CODE_OBJECT = 2;
    public static final int CODE_RECEIVE = 3;
    public static final int CODE_EXAMPLE = 4;

    public static final Message MSG_FACE;
    public static final Message MSG_OBJECT;
    public static final Message MSG_RECEIVE;
    public static final Message MSG_EXAMPLE;

    static {
        MSG_FACE = new Message();
        MSG_FACE.what = CODE_FACE;

        MSG_OBJECT = new Message();
        MSG_OBJECT.what = CODE_OBJECT;

        MSG_RECEIVE = new Message();
        MSG_RECEIVE.what = CODE_RECEIVE;

        MSG_EXAMPLE = new Message();
        MSG_EXAMPLE.what = CODE_EXAMPLE;
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
        switch (msg.what) {
            case CODE_RECEIVE:
                synchronized (this) {
                    isOperating = true;
                }
                receive((DataObject) msg.obj);
                break;
        }

        super.handleMessage(msg);
    }

    private void receive(DataObject object) {
        Fragment finalFaceFragment = getFaceFragment(object, null, "null",false);
        Fragment exampleFragment = getExampleFragment(object, finalFaceFragment, "face2");
        Fragment faceFragment = getFaceFragment(object, exampleFragment, "example", true);
        Fragment objectFragment = getObjectFragment(object, faceFragment, "face");

        postFragment(objectFragment, "object");
    }

    private Fragment getFaceFragment(DataObject object, Fragment next, String nextId, boolean speak) {
        final FaceFragment faceFragment = new FaceFragment();
        try {
            faceFragment.warp(context, object, speak);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        faceFragment.setListener(new FragmentFlowListener(next, nextId));

        return faceFragment;
    }

    public Fragment getObjectFragment(DataObject object, Fragment next, String nextId) {
        final ObjectShowerFragment objectShowerFragment = new ObjectShowerFragment();
        objectShowerFragment.warp(context, object);
        objectShowerFragment.setListener(new FragmentFlowListener(next, nextId));

        return objectShowerFragment;
    }

    private Fragment getExampleFragment(DataObject object, Fragment next, String nextId) {
        final ExampleShowerFragment fragment = new ExampleShowerFragment();
        fragment.warp(object);
        fragment.setListener(new FragmentFlowListener(next, nextId));
        return fragment;

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


    private class FragmentFlowListener implements FragmentListener {

        private final Fragment next;
        private final String nextId;

        private FragmentFlowListener(Fragment next, String nextId) {
            this.next = next;
            this.nextId = nextId;
        }

        @Override
        public void start() {

        }

        @Override
        public void end() {
            if (next != null)
                post(() -> postFragment(next, nextId));
        }
    }
}
