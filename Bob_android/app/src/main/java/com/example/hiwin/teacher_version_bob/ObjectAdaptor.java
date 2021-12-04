package com.example.hiwin.teacher_version_bob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.hiwin.teacher_version_bob.Constants.getObjectDrawableId;

/*
    reference:
        https://qiita.com/vc7/items/c863908b5273edd4fe53
        https://xnfood.com.tw/android-listview-baseadapter/
 */
public class ObjectAdaptor extends BaseAdapter {
    @android.support.annotation.NonNull
    private final Context context;
    private final JSONArray objects;
    private static LayoutInflater inflater = null;

    public ObjectAdaptor(Context context, JSONArray objects) {
        this.context = context;
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return objects.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.row_of_object, parent, false);

        try {
            JSONObject object = objects.getJSONObject(position).getJSONObject("data");

            String name = object.getString("name");
            viewHolder.img = (ImageView) convertView.findViewById(R.id.object_row_img);
            viewHolder.img.setImageDrawable(context.getDrawable(getObjectDrawableId(name)));
            viewHolder.name = (TextView) convertView.findViewById(R.id.object_row_name);
            viewHolder.name.setText(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name;
    }

}
