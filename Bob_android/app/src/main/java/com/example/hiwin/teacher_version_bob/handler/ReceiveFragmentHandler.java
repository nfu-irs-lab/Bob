package com.example.hiwin.teacher_version_bob.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;
import com.example.hiwin.teacher_version_bob.fragment.*;

import java.io.IOException;

public abstract class ReceiveFragmentHandler extends Handler {

    public static final int CODE_RECEIVE = 3;

    public static final Message MSG_RECEIVE;

    static {
        MSG_RECEIVE = new Message();
        MSG_RECEIVE.what = CODE_RECEIVE;
    }

    private final FragmentManager fragmentManager;
    private final Context context;

    protected abstract void onComplete();

    public ReceiveFragmentHandler(Context context, Looper looper, FragmentManager fragmentManager) {
        super(looper);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == CODE_RECEIVE) {
            receive((DataObject) msg.obj);
        }

        super.handleMessage(msg);
    }

    private void receive(DataObject object) {
        Fragment finalFaceFragment = getFinalFaceFragment(object,null,"null");
        Fragment exampleFragment = getExampleFragment(object, finalFaceFragment, "face2");
        Fragment faceFragment = getFaceFragment(object, exampleFragment, "example");
        Fragment objectFragment = getObjectFragment(object, faceFragment, "face");

        postFragment(objectFragment, "object");
    }


    private Fragment getFinalFaceFragment(DataObject object, Fragment next, String nextId) {
        final FaceFragment faceFragment = new FaceFragment();
        try {
            faceFragment.warp(context, object, false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        faceFragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            public void start() {
                super.start();
            }

            @Override
            public void end() {
                super.end();
                onComplete();
            }
        });

        return faceFragment;
    }

    private Fragment getFaceFragment(DataObject object, Fragment next, String nextId) {
        final FaceFragment faceFragment = new FaceFragment();
        try {
            faceFragment.warp(context, object, true,false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        faceFragment.setListener(new FragmentFlowListener(next,nextId));

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
