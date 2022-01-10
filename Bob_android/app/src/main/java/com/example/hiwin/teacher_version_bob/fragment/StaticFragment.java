package com.example.hiwin.teacher_version_bob.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


public abstract class StaticFragment extends Fragment {

    public interface ShowListener {
        void onShow(View[] views);
    }

    private FragmentListener listener;
    private ShowListener showListener;


    protected abstract View[] getViews();

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listener != null)
            listener.start();

        if (showListener != null)
            showListener.onShow(getViews());
    }


    public <L extends FragmentListener> void setListener(L listener) {
        this.listener = listener;
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }
}
