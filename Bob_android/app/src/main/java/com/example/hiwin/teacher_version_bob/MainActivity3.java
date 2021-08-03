package com.example.hiwin.teacher_version_bob;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.hiwin.teacher_version_bob.object.DataObject;
import com.example.hiwin.teacher_version_bob.object.ObjectShower;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        LayoutInflater inflater=getLayoutInflater();
        View a=inflater.inflate(R.layout.fragment_object,null);

        ObjectShower shower=new ObjectShower(a.findViewById(R.id.object_layout),R.id.object_img,R.id.object_name,R.id.object_tr_name);
        shower.warpObject(new DataObject("car","c","c","d"));

        ConstraintLayout root=(ConstraintLayout) findViewById(R.id.main3);
        ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        root.addView(a,params);
    }
}