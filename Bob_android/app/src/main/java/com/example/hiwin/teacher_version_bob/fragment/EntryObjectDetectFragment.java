package com.example.hiwin.teacher_version_bob.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.activity.InteractiveObjectDetectActivity;

import static com.example.hiwin.teacher_version_bob.Constants.getObjectDrawableId;

public class EntryObjectDetectFragment extends StaticFragment {

    private View root;
    private String answer;
    private String previous_detect;
    private ImageView img_answer;
    private TextView text_answer;

    private ImageView img_detect;
    private TextView text_detect;

    private TextView hint;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_inter_entry_object_detect, container, false);

        img_answer = (ImageView) root.findViewById(R.id.inter_obj_detect_entry_answer_img);
        text_answer = (TextView) root.findViewById(R.id.inter_obj_detect_entry_answer_text);
        img_detect = (ImageView) root.findViewById(R.id.inter_obj_detect_entry_detected_img);
        text_detect = (TextView) root.findViewById(R.id.inter_obj_detect_entry_detected_text);
        img_detect.setImageDrawable(null);
        text_detect.setText("");
        hint = (TextView) root.findViewById(R.id.inter_obj_detect_entry_hint);
        hint.setText("請將物品放在鏡頭前");

        return root;
    }

    public void setAnswer(String name) {
        answer = name;
        text_answer.setText(name);
        img_answer.setImageDrawable(getContext().getDrawable(getObjectDrawableId(name)));
    }

    public final InteractiveObjectDetectActivity.DetectedListener getListener() {
        return str -> {
            if (previous_detect == null || !previous_detect.equals(str)) {

                boolean correct = answer.equals(str);
                text_detect.setText(str);
                img_detect.setImageDrawable(getContext().getDrawable(getObjectDrawableId(str)));
                hint.setText(correct ? "答對了" : "答錯了");
                MediaPlayer mp = MediaPlayer.create(getContext(), correct ? R.raw.sound_correct : R.raw.sound_wrong);
                mp.start();
                previous_detect = str;
            }
        };
    }

    @Override
    protected View[] getViews() {
        return new View[0];
    }
}
