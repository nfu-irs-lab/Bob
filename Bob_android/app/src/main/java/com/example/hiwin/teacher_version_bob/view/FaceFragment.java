package com.example.hiwin.teacher_version_bob.view;

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
import com.example.hiwin.teacher_version_bob.object.JObject;

import java.io.IOException;

public class FaceFragment extends Fragment implements FaceFragmentListener {
    FaceController faceController;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FaceFragmentListener listener;

    private JObject object;
    public void setListener(FaceFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_face, container, false);
        ImageView imgFace = (ImageView) root.findViewById(R.id.face_gif);
        faceController=new FaceController(imgFace,getResources());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=getArguments();

        faceController.setListener(new FaceController.FaceListener() {
            ObjectSpeaker speaker;
            @Override
            public void onFaceMotionStarted(FaceController controller) {
                speaker=new ObjectSpeaker(getContext());
                speaker.setSpeakerListener(new ObjectSpeaker.SpeakerListener() {
                    @Override
                    public void onSpeakComplete() {
                    }
                });

                speaker.speak(object);
            }

            @Override
            public void onFaceMotionComplete(FaceController controller) {
                complete();
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


    @Override
    public void start() {
        listener.start();
    }

    @Override
    public void complete() {
        listener.complete();
    }

    public void setObject(JObject object) {
        this.object = object;
    }
}
