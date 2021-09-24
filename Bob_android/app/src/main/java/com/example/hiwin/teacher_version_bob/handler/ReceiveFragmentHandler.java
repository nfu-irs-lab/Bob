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
        try {
            if (msg.what == CODE_RECEIVE) {
                receive((DataObject) msg.obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.handleMessage(msg);
    }

    private void receive(DataObject object) throws Exception {
        Fragment finalFaceFragment = getFinalFaceFragment(getFace(object), null, "null");
        Fragment exampleFragment = getExampleFragment(object, finalFaceFragment, "face2");
        Fragment faceFragment = getFaceFragment(object, exampleFragment, "example");
        Fragment objectFragment = getObjectFragment(object, faceFragment, "face");
        postFragment(objectFragment, "object");
    }


    private Fragment getFinalFaceFragment(FaceFragment.Face face, Fragment next, String nextId) throws IOException {
        FaceFragment faceFragment = new FaceFragment();
        faceFragment.warp(context, face, 2, true);
        faceFragment.setListener(new FragmentFlowListener(next, nextId){
            @Override
            public void end() {
                super.end();
                onComplete();
            }
        });
        return faceFragment;
    }

    private Fragment getFaceFragment(DataObject object, Fragment next, String nextId) throws IOException {

        ObjectSpeaker speaker = new ObjectSpeaker(context);
        FaceFragment faceFragment = new FaceFragment();
        faceFragment.warp(context, getFace(object), 5, false);

        faceFragment.setListener(new FragmentFlowListener(next, nextId) {
            @Override
            public void start() {
                super.start();
                speaker.setSpeakerListener(this::end);
                speaker.speakFully(object);
            }

            @Override
            public void end() {
                super.end();
            }
        });

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

    private FaceFragment.Face getFace(DataObject object) {
        String name = object.getName();
        switch (name) {
            case "car":
                return FaceFragment.Face.sad;
            case "cake":
            case "knife":
            case "bowl":
            case "person":
            case "bird":
            case "cat":
            case "bottle":
                return FaceFragment.Face.happy;
            default:
                throw new RuntimeException("unknown face.");
        }
    }
}
