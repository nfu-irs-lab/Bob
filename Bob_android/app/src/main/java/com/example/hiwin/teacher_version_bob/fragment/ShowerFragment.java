package com.example.hiwin.teacher_version_bob.fragment;

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

public class ShowerFragment extends Fragment {

    private ShowerListener showerListener;

    public interface ShowerListener{
        void onShow(ImageView imageView,TextView textView1,TextView textView2);
    }

    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    private FragmentListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_object, container, false);
        View layout = root.findViewById(R.id.object_layout);

        this.imageView = (ImageView) layout.findViewById(R.id.shower_imageview);
        this.textView1 = (TextView) layout.findViewById(R.id.shower_textview1);
        this.textView2 = (TextView) layout.findViewById(R.id.shower_textview2);

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


        showerListener.onShow(imageView, textView1, textView2);
    }


    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }

    public void setShowerListener(ShowerListener showerListener){
        this.showerListener =showerListener;
    }
}
