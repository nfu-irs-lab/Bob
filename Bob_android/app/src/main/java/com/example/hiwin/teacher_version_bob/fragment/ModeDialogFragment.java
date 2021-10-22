package com.example.hiwin.teacher_version_bob.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ModeDialogFragment extends DialogFragment {
    public enum Mode {
        face_detect("Face Detect"), object_detect("Object Detect");

        final String description;

        Mode(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public interface OptionListener {
        void select(Mode mode);
    }

    private OptionListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Mode[] list=Mode.values();
        String[] items=new String[list.length];
        Arrays.stream(Mode.values()).map(Mode::getDescription).collect(Collectors.toList()).toArray(items);
        builder.setTitle("Mode")
                .setItems(items, (dialog, which) -> {
                    if (listener != null)
                        listener.select(list[which]);
                });
        return builder.create();
    }

    public void setListener(OptionListener listener) {
        this.listener = listener;
    }
}