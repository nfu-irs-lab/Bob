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

import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.object.JObject;
import com.example.hiwin.teacher_version_bob.object.ObjectShower;

public class ObjectShowerFragment extends Fragment {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ObjectShower objectShower;
    private JObject object;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_object, container, false);
        View object_layout=root.findViewById(R.id.object_layout);
        objectShower=new ObjectShower(object_layout,R.id.object_img,R.id.object_name,R.id.object_tr_name);
        objectShower.hind();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                objectShower.warpObject(object);
                objectShower.show();
            }
        });
    }

    public void setObject(JObject object) {
        this.object = object;
    }
}
