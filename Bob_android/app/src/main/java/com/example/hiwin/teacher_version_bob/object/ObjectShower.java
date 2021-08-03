package com.example.hiwin.teacher_version_bob.object;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiwin.teacher_version_bob.R;

public class ObjectShower {
    private final ImageView img;
    private final TextView name;
    private final TextView tr_name;
    private final View layout;

    public ObjectShower(View layout,int object_image_id,int object_name_id,int object_tr_name_id) {
        this.layout=layout;
        this.img =(ImageView) layout.findViewById(object_image_id);
        this.name = (TextView) layout.findViewById(object_name_id);
        this.tr_name = (TextView) layout.findViewById(object_tr_name_id);
    }

    public void warpObject(DataObject object){
        img.setImageDrawable(layout.getContext().getDrawable(getDrawableId(object)));
        this.name.setText(object.getName());
        this.tr_name.setText(object.getTranslatedName());
    }

    public void show(){
        layout.setVisibility(View.VISIBLE);
    }

    public void hind(){
        layout.setVisibility(View.INVISIBLE);
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
        throw  new RuntimeException("Drawable not found");
    }
}
