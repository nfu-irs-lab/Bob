package com.example.hiwin.teacher_version_bob.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.hiwin.teacher_version_bob.R;

public class ObjectSelectFragment extends StaticFragment {

    public abstract static class ItemSelectListener implements FragmentListener {
        public abstract void onItemSelected(int position);
    }

    private ItemSelectListener selectListener;

    private ListView listView;
    private View root;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_inter_obj_object_select, container, false);
        listView = (ListView) root.findViewById(R.id.inter_obj_object_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectListener != null) {
                    selectListener.onItemSelected(position);
                    selectListener.end();
                }
            }
        });
        return root;
    }

    @Override
    protected View[] getViews() {
        View[] views = new View[1];
        views[0] = listView;
        return views;
    }

    @Override
    public <L extends FragmentListener> void setFragmentListener(L commandListener) {
        selectListener = (ItemSelectListener) commandListener;
    }
}
