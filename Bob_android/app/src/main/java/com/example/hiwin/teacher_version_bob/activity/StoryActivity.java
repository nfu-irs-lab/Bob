package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.StoryAdapter;
import com.example.hiwin.teacher_version_bob.fragment.StaticFragment;
import com.example.hiwin.teacher_version_bob.fragment.StoriesSelectFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class StoryActivity extends BluetoothCommunicationActivity {

    private JSONObject selectedObject;

    @Override
    protected void receive(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8);
        try {
            JSONObject obj = new JSONObject(str);
            String content = obj.getString("content");
            if (content.equals("all_stories_info")) {
                JSONArray array = obj.getJSONArray("data");
                Fragment fragment = getSelectFragment(array, null, null);
                postFragment(fragment, "stories select");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(getClass().getSimpleName(), str);
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.story_toolbar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_story;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected String getDeviceAddress(Bundle savedInstanceState) {
        Intent it = getIntent();
        return it.getStringExtra("address");
    }

    @Override
    protected void onConnect() {
        sendMessage("STORY_GET LIST");

    }

    @Override
    protected void onDisconnect() {

    }

    private Fragment getSelectFragment(JSONArray array, Fragment next, String nextId) {
        StaticFragment selectFragment = new StoriesSelectFragment();
        selectFragment.setShowListener(views -> ((ListView) views[0]).setAdapter(new StoryAdapter(this, array)));
        selectFragment.setListener(new StoriesSelectFragment.ItemSelectListener() {

            @Override
            public void onItemSelected(int position) {
                try {
                    selectedObject = array.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void start() {

            }

            @Override
            public void end() {
                try {
                    Toast.makeText(StoryActivity.this,selectedObject.getString("id")+"",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                postFragment(next, nextId);
            }
        });
        return selectFragment;
    }

    private void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.story_frame, fragment, id);
        fragmentTransaction.commit();
    }

}
