package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.StoryAdapter;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class StoryActivity extends BluetoothCommunicationActivity {

    private JSONObject selectedObject;
    private int groupA_score = 0;
    private int groupB_score = 0;

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
//                StaticFragment vocabulariesFragment = getAllVocabulary(dataObj.getJSONArray("vocabularies"), null);
//                StaticFragment storyFragment = getAllStory(dataObj.getJSONArray("pages"), vocabulariesFragment);
//                postFragment(storyFragment, "story");


                VocabularyInteractiveFragment aaa = new VocabularyInteractiveFragment();

                JSONArray vocabularies = dataObj.getJSONArray("vocabularies");
                aaa.initialize(this, vocabularies);
                postFragment(aaa,"A");

//                JSONObject[] chosen=new JSONObject[10];
//                JSONArray rest_of_the_words=new JSONArray();
//                for (int i=0;i<vocabularies.length();i++){
//                    rest_of_the_words.put(vocabularies.get(i));
//                }
//
//                for (int i=0;i<chosen.length;i++){
//                    int chosen_index=(int)(Math.random()*rest_of_the_words.length());
//                    chosen[i]=rest_of_the_words.getJSONObject(chosen_index);
//                    rest_of_the_words.remove(chosen_index);
//                }
//
//
//                StaticFragment fragment = getVocabularyInteractiveFragment(chosen[0],vocabularies , null);
//                StaticFragment fragment2 = getVocabularyInteractiveFragment(chosen[1], vocabularies, fragment);
//                postFragment(fragment2, "story");
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

    private StaticFragment getAllStory(JSONArray pages, StaticFragment external_next) throws JSONException {
        StaticFragment next = external_next;
        for (int i = pages.length() - 1; i >= 0; i--) {
            JSONObject page = pages.getJSONObject(i);
            next = getStoryPageFragment(page, next, "page" + (i + 1));
        }
        return next;
    }

    private StaticFragment getAllVocabulary(JSONArray vocabularies, StaticFragment external_next) throws JSONException {
        StaticFragment next = external_next;
        for (int i = vocabularies.length() - 1; i >= 0; i--) {
            JSONObject vocabulary = vocabularies.getJSONObject(i);
            next = getVocabularyFragment(vocabulary, next, "vocabulary" + (i + 1));
        }
        return next;
    }

//    private StaticFragment getVocabularyInteractiveFragment(JSONObject vocabulary, JSONArray vocabularies, StaticFragment external_next) throws JSONException {
//        VocabularyInteractiveFragment fragment = new VocabularyInteractiveFragment();
//
//        final int drawable_id = getResourceIDByString(vocabulary.getString("image"), "raw");
//        final Drawable drawable = drawable_id <= 0 ? null : getDrawable(drawable_id);
//
//        String correct_name = vocabulary.getString("name");
//
//        List<String> wrong_options = new LinkedList<>();
//        for (int i = 0; i < vocabularies.length(); i++) {
//            String wrong_name = vocabularies.getJSONObject(i).getString("name");
//            if (!wrong_name.equals(correct_name))
//                wrong_options.add(wrong_name);
//        }
//
//        String[] options = new String[4];
//        int correct_index = (int) (Math.random() * 4);
//        options[correct_index] = correct_name;
//
//        for (int i = 0; i < options.length; i++) {
//            if (i == correct_index)
//                continue;
//
//            int j = (int) (Math.random() * wrong_options.size());
//            options[i] = wrong_options.get(j);
//            wrong_options.remove(j);
//        }
//
//
//        fragment.initialize(groupA_score,drawable, options, correct_name);
//        fragment.setListener(new VocabularyInteractiveFragment.AnswerListener() {
//            @Override
//            public void onAnswerCorrect(String selected) {
//                Toast.makeText(StoryActivity.this, "Correct", Toast.LENGTH_SHORT).show();
//                groupA_score++;
//                end();
//            }
//
//            @Override
//            public void onAnswerIncorrect(String selected, String correct) {
//                Toast.makeText(StoryActivity.this, "Incorrect ->" + correct, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void start() {
//
//            }
//
//            @Override
//            public void end() {
//                postFragment(external_next,correct_name);
//            }
//        });
//
//        return fragment;
//    }

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

    private StaticFragment getVocabularyFragment(JSONObject vocabulary, Fragment next, String nextId) throws JSONException {

        VocabularyFragment storyPageFragment = new VocabularyFragment();
        final int drawable_id = getResourceIDByString(vocabulary.getString("image"), "raw");
        final Drawable drawable = drawable_id <= 0 ? null : getDrawable(drawable_id);
        final int audio_id = getResourceIDByString(vocabulary.getString("audio"), "raw");
        String name = vocabulary.getString("name");
        String translated = vocabulary.getString("translated");
        String part_of_speech = vocabulary.getString("part_of_speech");
        storyPageFragment.setShowListener(views -> {
            ((ImageView) views[0]).setImageDrawable(drawable);
            ((TextView) views[1]).setText(name + " (" + part_of_speech + ".)");
            ((TextView) views[2]).setText(translated);
        });

        storyPageFragment.setListener(new FragmentListener() {
            @Override
            public void start() {
                final MediaPlayer player = MediaPlayer.create(StoryActivity.this, audio_id);
                player.start();
                player.setOnCompletionListener(mp -> {
                    player.release();
                    end();
                });
            }

            @Override
            public void end() {
                postFragment(next, nextId);
            }
        });
        return storyPageFragment;
    }

    private StaticFragment getStoryPageFragment(JSONObject page, Fragment next, String nextId) throws JSONException {
        StoryPageFragment storyPageFragment = new StoryPageFragment();
        final String text = page.getString("text");
        final int audio_id = getResourceIDByString(page.getString("audio"), "raw");
        final int drawable_id = getResourceIDByString(page.getString("image"), "raw");
        final Drawable drawable = drawable_id <= 0 ? null : getDrawable(drawable_id);

        storyPageFragment.setShowListener(views -> {
            ((ImageView) views[0]).setImageDrawable(drawable);
            ((TextView) views[1]).setText(text);
        });

        storyPageFragment.setListener(new FragmentListener() {
            @Override
            public void start() {
                final MediaPlayer player;
                player = MediaPlayer.create(StoryActivity.this, audio_id);
                player.start();
                player.setOnCompletionListener(mp -> {
                            player.release();
                            end();
                        }
                );
            }

            @Override
            public void end() {
                postFragment(next, nextId);
            }
        });
        return storyPageFragment;
    }

    private void postFragment(Fragment fragment, String id) {
        if (fragment == null || id == null)
            return;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.story_frame, fragment, id);
        fragmentTransaction.commit();
    }

    private int getResourceIDByString(String resName, String type) {
        return getApplicationContext().getResources()
                .getIdentifier(resName
                        , type
                        , getPackageName());
    }

}
