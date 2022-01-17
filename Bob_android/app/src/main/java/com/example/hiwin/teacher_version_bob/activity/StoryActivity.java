package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.*;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.StoryAdapter;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class StoryActivity extends BluetoothCommunicationActivity {
    private JSONObject selectedObject;

    @Override
    protected void receive(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8);
        Log.d(getClass().getSimpleName(), str);
        try {
            JSONObject obj = new JSONObject(str);
            String content = obj.getString("content");
            if (content.equals("all_stories_info")) {
                JSONArray array = obj.getJSONArray("data");
                Fragment fragment = getSelectFragment(array, null, null);
                postFragment(fragment, "stories select");
            } else if (content.equals("story_content")) {
                JSONObject dataObj = obj.getJSONObject("data");

                JSONArray vocabularies=dataObj.getJSONArray("vocabularies");

                StaticFragment  vocabularyInteractiveFragment=getVocabularyInteractiveFragment(vocabularies,null,null);
                StaticFragment storyFragment = getStoryPageFragment(dataObj.getJSONArray("pages"), vocabularyInteractiveFragment, "vocabularyInteractiveFragment");
                StaticFragment vocabularyFragment=getVocabularyFragment(vocabularies,storyFragment,"storyFragment");
                postFragment(vocabularyFragment, "story");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    String id = selectedObject.getString("id");
                    Toast.makeText(StoryActivity.this, id, Toast.LENGTH_SHORT).show();
                    sendMessage("STORY_GET STORY " + id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return selectFragment;
    }

    private StaticFragment getVocabularyFragment(JSONArray vocabularies, Fragment next, String nextId){
        VocabularyFragment vocabularyFragment = new VocabularyFragment();
        vocabularyFragment.initialize(this,vocabularies);
        vocabularyFragment.setListener(new VocabularyFragment.ActionListener() {
            @Override
            public void onAction(String cmd) {
                sendMessage(cmd);
            }

            @Override
            public void start() {

            }

            @Override
            public void end() {
                postFragment(next,nextId);
            }
        });
        return vocabularyFragment;
    }

    private StaticFragment getStoryPageFragment(JSONArray pages, Fragment next, String nextId) throws JSONException {
        StoryPageFragment storyPageFragment = new StoryPageFragment();
        storyPageFragment.initialize(this, pages);
        storyPageFragment.setListener(new FragmentListener() {
            @Override
            public void start() {

            }

            @Override
            public void end() {
                postFragment(next, nextId);
            }
        });
        return storyPageFragment;
    }

    private StaticFragment getVocabularyInteractiveFragment(JSONArray vocabularies, Fragment next, String nextId){
        VocabularyInteractiveFragment vocabularyInteractiveFragment=new VocabularyInteractiveFragment();
        try {
            vocabularyInteractiveFragment.initialize(this,vocabularies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        vocabularyInteractiveFragment.setListener(new VocabularyInteractiveFragment.AnswerListener() {
            @Override
            public void onAnswerCorrect() {
                sendMessage("DO_ACTION correct.csv");
            }

            @Override
            public void onAnswerIncorrect() {
                sendMessage("DO_ACTION incorrect.csv");
            }

            @Override
            public void start() {

            }

            @Override
            public void end() {

            }
        });

        return vocabularyInteractiveFragment;
    }

    private void postFragment(Fragment fragment, String id) {
        if (fragment == null || id == null)
            return;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.story_frame, fragment, id);
        fragmentTransaction.commit();
    }
}
