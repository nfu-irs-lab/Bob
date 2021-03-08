package com.example.hiwin.teacher_version_bob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/*
    reference:
        https://qiita.com/vc7/items/c863908b5273edd4fe53
        https://xnfood.com.tw/android-listview-baseadapter/
 */
public class DetectedObjectAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<HashMap<String,Object>> objects;
    private static LayoutInflater inflater = null;

    public DetectedObjectAdapter(Context context, ArrayList<HashMap<String,Object>> objects) {
        this.context = context;
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        convertView = inflater.inflate(R.layout.row_of_detect_result, parent, false);
        // 把拿到的 textView 設定進 view holder
        viewHolder.object_name = (TextView) convertView.findViewById(R.id.detect_result_row_name);
        viewHolder.object_tr_name = (TextView) convertView.findViewById(R.id.detect_result_row_tr_name);
        viewHolder.object_sentence = (TextView) convertView.findViewById(R.id.detect_result_row_sentence);
        viewHolder.object_tr_sentence = (TextView) convertView.findViewById(R.id.detect_result_row_tr_sentence);

        viewHolder.object_name.setText((String)objects.get(position).get("name"));
        viewHolder.object_tr_name.setText((String)objects.get(position).get("tr_name"));
        viewHolder.object_sentence.setText((String)objects.get(position).get("sentence"));
        viewHolder.object_tr_sentence.setText((String)objects.get(position).get("tr_sentence"));

        return convertView;
    }

    static class ViewHolder {
        TextView object_name;
        TextView object_sentence;
        TextView object_tr_name;
        TextView object_tr_sentence;
    }

    private String parseBondState(int value) {
        switch (value) {
            case 0xa:
                return "BOND_NONE";
            case 0xb:
                return "BOND_BONDING";
            case 0xc:
                return "BOND_BONDED";
            default:
                return null;
        }
    }
}
