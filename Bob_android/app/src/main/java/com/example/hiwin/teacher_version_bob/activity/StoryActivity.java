package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.StoryAdapter;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StoryActivity extends BluetoothCommunicationActivity {
    private MenuItem item_next;
    private StaticFragment[] fragments;
    private int progress = 0;

    @Override
    protected void receive(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8);
        Log.d(getClass().getSimpleName(), str);
        try {
            JSONObject obj = new JSONObject(str);
            String content = obj.getString("content");
            if (content.equals("all_stories_info")) {
                JSONArray array = obj.getJSONArray("data");
                postFragment(getSelectFragment(array));
            } else if (content.equals("story_content")) {
                JSONObject dataObj = obj.getJSONObject("data");
                fragments = new StaticFragment[4];
                for (int i = 0; i < fragments.length; i++) {
                    fragments[i] = fragmentSelector(i, dataObj);
                }
                postFragment(fragments[progress]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private StaticFragment fragmentSelector(int progress, JSONObject dataObject) throws JSONException {
        switch (progress) {
            case 0:
            case 3:
                return getStoryPageFragment(dataObject.getJSONArray("pages"));
            case 1:
                return getPaperScissorStoneFragment();
            case 2:
                return getVocabularyFragment(dataObject.getJSONArray("vocabularies"));
            default:
                throw new IllegalStateException();
        }

    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.story_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item_next = menu.add("Next");
        item_next.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == item_next) {
            if (fragments != null && progress < fragments.length - 1) {
                fragments[progress].interrupt();
                postFragment(fragments[++progress]);
            }
        }
        return super.onOptionsItemSelected(item);
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

    private Fragment getSelectFragment(JSONArray array) {
        StoriesSelectFragment selectFragment = new StoriesSelectFragment();
        selectFragment.setShowListener(views -> ((ListView) views[0]).setAdapter(new StoryAdapter(this, array)));
        selectFragment.setSelectListener(new StoriesSelectFragment.ItemSelectListener() {

            @Override
            public void onItemSelected(int position) {
                try {
                    JSONObject selectedObject = array.getJSONObject(position);
                    try {
                        String id = selectedObject.getString("id");
                        sendMessage("STORY_GET STORY " + id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return selectFragment;
    }

    private StaticFragment getVocabularyFragment(JSONArray vocabularies) {
        VocabularyFragment vocabularyFragment = new VocabularyFragment();
        vocabularyFragment.initialize(this, vocabularies);
        vocabularyFragment.setCommandListener(this::sendMessage);
        return vocabularyFragment;
    }

    private StaticFragment getStoryPageFragment(JSONArray pages) throws JSONException {
        StoryPageFragment storyPageFragment = new StoryPageFragment();
        storyPageFragment.initialize(this, pages);
        storyPageFragment.setCommandListener(this::sendMessage);
        return storyPageFragment;
    }

    private StaticFragment getPaperScissorStoneFragment() {
        PaperScissorStoneFragment fragment = new PaperScissorStoneFragment();
        fragment.initialize(this);
        fragment.setCommandListener(this::sendMessage);
        return fragment;
    }

    private StaticFragment getVocabularyInteractiveFragment(JSONArray vocabularies, Fragment next, String nextId) {
        VocabularyInteractiveFragment vocabularyInteractiveFragment = new VocabularyInteractiveFragment();
        try {
            vocabularyInteractiveFragment.initialize(this, vocabularies);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vocabularyInteractiveFragment.setFragmentListener(new FragmentListener() {
            @Override
            public void start() {

            }

            @Override
            public void end() {
                StoryActivity.this.finish();
            }
        });

        vocabularyInteractiveFragment.setCommandListener(this::sendMessage);
        return vocabularyInteractiveFragment;
    }


    private void postFragment(Fragment fragment) {
        if (fragment == null) return;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.story_frame, fragment, UUID.randomUUID().toString());
        fragmentTransaction.commit();
    }
}
