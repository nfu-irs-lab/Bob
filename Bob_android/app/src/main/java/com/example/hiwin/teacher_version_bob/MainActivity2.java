package com.example.hiwin.teacher_version_bob;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.support.design.widget.DrawableUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MainActivity2 extends AppCompatActivity {

    static class ViewHolder {
        static class Face {
            static ImageView img;
            static ImageView img2;
            static ImageView img3;
        }

        static class ObjectShower {
            static LinearLayout object_layout;
            static ImageView img_object;
            static TextView name;
            static TextView tr_name;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ViewHolder.Face.img = (ImageView) findViewById(R.id.imageView1);
        ViewHolder.Face.img2 = (ImageView) findViewById(R.id.imageView2);
        ViewHolder.Face.img3 = (ImageView) findViewById(R.id.imageView);

        ViewHolder.Face.img.setVisibility(View.INVISIBLE);
        ViewHolder.Face.img2.setVisibility(View.INVISIBLE);
        ViewHolder.Face.img3.setVisibility(View.INVISIBLE);


        ViewHolder.ObjectShower.object_layout = (LinearLayout) findViewById(R.id.layout_image);
        ViewHolder.ObjectShower.object_layout.setVisibility(View.INVISIBLE);
        ViewHolder.ObjectShower.img_object = (ImageView) findViewById(R.id.object);
        ViewHolder.ObjectShower.name = (TextView) findViewById(R.id.object_name);
        ViewHolder.ObjectShower.tr_name = (TextView) findViewById(R.id.object_tr_name);
    }

    void showObject(String name,String tr_name){
        ViewHolder.ObjectShower.object_layout.setVisibility(View.VISIBLE);
        ViewHolder.ObjectShower.img_object.setImageDrawable(getResources().getDrawable(R.drawable.ic_eco_car));
        ViewHolder.ObjectShower.name.setText(name);
        ViewHolder.ObjectShower.tr_name.setText(tr_name);
    }

    public void start(View v) {
        showObject("Car","cccc");
//        ViewHolder.Face.img.setVisibility(View.VISIBLE);
//        ViewHolder.Face.img2.setVisibility(View.VISIBLE);
//        ViewHolder.Face.img3.setVisibility(View.VISIBLE);
//        FaceAdaptor faceAdaptor = new FaceAdaptor(ViewHolder.Face.img, ViewHolder.Face.img2, ViewHolder.Face.img3);
//        runOnUiThread(faceAdaptor);

    }
}