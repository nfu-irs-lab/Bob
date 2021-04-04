package com.example.hiwin.teacher_version_bob.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class ObjectShowerFragment extends Fragment {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ObjectShower objectShower;
    private HashMap<String,Object> datas;
    public ObjectShowerFragment(HashMap<String,Object> datas){
        this.datas=datas;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_content_object, container, false);
        objectShower=new ObjectShower(root);
        objectShower.hind();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                objectShower.setData((Drawable) datas.get("img"),datas.get("name").toString(),datas.get("tr_name").toString());
                objectShower.show();
            }
        });
    }


}
