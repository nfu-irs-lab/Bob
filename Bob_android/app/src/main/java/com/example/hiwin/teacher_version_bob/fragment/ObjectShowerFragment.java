package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;

public class ObjectShowerFragment extends Fragment {
    private DataObject object;
    private Drawable drawable;
    private TextView tr_name;
    private ImageView img;
    private TextView name;
    private FragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_object, container, false);
        View layout = root.findViewById(R.id.object_layout);

        this.img = (ImageView) layout.findViewById(R.id.object_img);
        this.name = (TextView) layout.findViewById(R.id.object_name);
        this.tr_name = (TextView) layout.findViewById(R.id.object_tr_name);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listener != null)
            listener.start();

        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (listener != null)
                listener.end();
        }).start();

        this.img.setImageDrawable(drawable);
        this.name.setText(object.getName());
        this.tr_name.setText(object.getTranslatedName());
    }

    public void warp(Context context, DataObject object) {
        this.object = object;
        drawable = context.getDrawable(getDrawableId(object));
    }

    private int getDrawableId(DataObject object) {
        switch (object.getName()) {
            case "car":
                return R.drawable.object_car;
            case "knife":
                return R.drawable.object_knife;
            case "cake":
                return R.drawable.object_cake;
            case "bird":
                return R.drawable.object_bird;
            case "bowl":
                return R.drawable.object_bowl;
            case "person":
                return R.drawable.object_person;
            case "cat":
                return R.drawable.object_cat;
            case "bottle":
                return R.drawable.object_bottle;
        }
        throw new RuntimeException("Drawable not found");
    }

    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }
}
