package com.example.hiwin.teacher_version_bob.object;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiwin.teacher_version_bob.R;

public class ObjectShower {
    private final ImageView img;
    private final TextView name;
    private final TextView tr_name;
    private final View root;
    private final View layout;

    public ObjectShower(View root) {
        this.root=root;
        this.layout=root.findViewById(R.id.object_layout);
        this.img =(ImageView) layout.findViewById(R.id.object_img);
        this.name = (TextView) layout.findViewById(R.id.object_name);
        this.tr_name = (TextView) layout.findViewById(R.id.object_tr_name);
    }

    public void warpObject(JObject object){
        img.setImageDrawable(root.getContext().getDrawable(object.getDrawableId()));
        this.name.setText(object.getName());
        this.tr_name.setText(object.getTranslatedName());
    }

    public void show(){
        layout.setVisibility(View.VISIBLE);
    }

    public void hind(){
        layout.setVisibility(View.INVISIBLE);
    }

    public View getRoot() {
        return root;
    }

}
