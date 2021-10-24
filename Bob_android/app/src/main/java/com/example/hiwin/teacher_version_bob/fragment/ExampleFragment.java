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

public class ExampleFragment extends Fragment {
    public interface ShowListener {
        void onShow(TextView textView1, TextView textView2);
    }

    private TextView textView2;
    private TextView textView1;
    private FragmentListener listener;
    private ShowListener showListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_example, container, false);
        View layout = root.findViewById(R.id.example_layout);
        this.textView1 = (TextView) layout.findViewById(R.id.example_sentence);
        this.textView2 = (TextView) layout.findViewById(R.id.example_tr_sentence);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listener != null)
            listener.start();

        showListener.onShow(textView1, textView2);
    }

    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }
}
