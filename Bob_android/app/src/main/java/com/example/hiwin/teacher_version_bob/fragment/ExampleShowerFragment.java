package com.example.hiwin.teacher_version_bob.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.data.ObjectSpeaker;
import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;

public class ExampleShowerFragment extends Fragment {
    private DataObject object;
    private TextView tr_sentence;
    private TextView sentence;
    private ObjectSpeaker speaker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_example, container, false);
        View layout = root.findViewById(R.id.example_layout);
        speaker=new ObjectSpeaker(getContext());
        this.sentence = (TextView) layout.findViewById(R.id.example_sentence);
        this.tr_sentence = (TextView) layout.findViewById(R.id.example_tr_sentence);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sentence.setText(object.getSentence());
        tr_sentence.setText(object.getTranslatedSentence());
        speaker.speakExample(object);
    }

    public void warp(DataObject object) {
        this.object = object;
    }


}
