package com.example.hiwin.teacher_version_bob.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.hiwin.teacher_version_bob.R;

import static com.example.hiwin.teacher_version_bob.Constants.getResourceIDByString;


public class EntryObjectDetectFragment extends StaticFragment {

    private String definition;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inter_entry_object_detect, container, false);
        TextView definition = (TextView) root.findViewById(R.id.inter_obj_detect_definition);
        definition.setText(this.definition);
        return root;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    protected View[] getViews() {
        return new View[0];
    }

    @Override
    public void interrupt() {

    }
}
