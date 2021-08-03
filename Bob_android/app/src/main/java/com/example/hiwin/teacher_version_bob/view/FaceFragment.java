package com.example.hiwin.teacher_version_bob.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import com.example.hiwin.teacher_version_bob.object.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.object.DataObject;

import java.io.IOException;

public class FaceFragment extends Fragment {
    FaceController faceController;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FaceFragmentListener listener;

    private DataObject object;

    public void setListener(FaceFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_face, container, false);
        ImageView imgFace = (ImageView) root.findViewById(R.id.face_gif);
        faceController = new FaceController(imgFace, getResources());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context=getContext();
        final ObjectSpeaker speaker = new ObjectSpeaker(context);
        speaker.setSpeakerListener(() -> faceController.hind());

        faceController.setListener(new FaceController.FaceListener() {
            @Override
            public void onFaceMotionStarted(FaceController controller) {
                if(listener!=null)
                    listener.start(faceController);
                speaker.speak(object);
            }

            @Override
            public void onFaceMotionComplete(FaceController controller) {
                if(listener!=null)
                    listener.complete(faceController);
            }
        });

        try {
            faceController.warp(FaceController.FaceType.valueOf(object.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        faceController.show();
        faceController.start();
    }



    public void setObject(DataObject object) {
        this.object = object;
    }
}
