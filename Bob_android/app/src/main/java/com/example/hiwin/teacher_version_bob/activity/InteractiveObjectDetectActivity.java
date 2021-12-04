package com.example.hiwin.teacher_version_bob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.ObjectAdaptor;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.communication.bluetooth.framework.SerialListener;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class InteractiveObjectDetectActivity extends BluetoothCommunicationActivity {


    private static class SelectedObject {
        private final String name;
        private final String tr_name;
        private final String sentence;
        private final String tr_sentence;

        private SelectedObject(String name, String tr_name, String sentence, String tr_sentence) {
            this.name = name;
            this.tr_name = tr_name;
            this.sentence = sentence;
            this.tr_sentence = tr_sentence;
        }

        public String getName() {
            return name;
        }

        public String getTrName() {
            return tr_name;
        }

        public String getSentence() {
            return sentence;
        }

        public String getTrSentence() {
            return tr_sentence;
        }

        public static class Builder {
            public SelectedObject buildFromJsonObject(JSONObject jsonObject) throws JSONException {
                JSONObject jdata = jsonObject.getJSONObject("data");

                JSONArray languages = jdata.getJSONArray("languages");

                JSONObject translated = null;
                for (int i = 0; i < languages.length(); i++) {
                    if (languages.getJSONObject(i).get("code").equals("zh_TW"))
                        translated = languages.getJSONObject(i);
                }

                if (translated == null)
                    throw new RuntimeException("code not found");

//                Face face = Face.valueOf(jdata.getString("face"));
                String name = jdata.getString("name");
                String sentence = jdata.getString("sentence");
                String tr_name = translated.getString("tr_name");
                String tr_sentence = translated.getString("tr_sentence");
                return new SelectedObject(name, tr_name, sentence, tr_sentence);
            }
        }

    }

    private static final String THIS_LOG_TAG = "InteractiveObjectDetectActivity";
    private SelectedObject selectedObject;
    private DetectedListener detectedListener;

    @Override
    protected void initialize(Bundle savedInstanceState) {
    }

    @Override
    protected String getDeviceAddress(Bundle savedInstanceState) {
        Intent it = getIntent();
        return it.getStringExtra("address");
    }

    @Override
    protected void receive(byte[] data) {
        try {
            String str = new String(data, StandardCharsets.UTF_8);
            Log.d(THIS_LOG_TAG, "received string:");
            Log.d(THIS_LOG_TAG, str);
            JSONObject object = new JSONObject(str);
//            int id = object.getInt("id");
//            String responseType = object.getString("response_type");
            String content = object.getString("content");
            if (content.equals("all_object")) {
                Fragment entryDetectFragment = getEntryDetectFragment();
                Fragment selectFragment = getSelectFragment(object.getJSONArray("data"), entryDetectFragment, "entryDetectFragment");
                postFragment(selectFragment, "selectFragment");
            } else if (content.equals("single_object")) {
                SelectedObject.Builder builder = new SelectedObject.Builder();
                SelectedObject sb = builder.buildFromJsonObject(object);
                if (detectedListener == null)
                    throw new RuntimeException("detectedListener");

                detectedListener.onDetected(sb.getName());
            } else {
                throw new RuntimeException("Unknown state");
            }


        } catch (IllegalArgumentException | JSONException e) {
            Log.e(THIS_LOG_TAG, e.getMessage());
        }
    }

    private Fragment getSelectFragment(JSONArray array, Fragment next, String nextId) {
        ObjectSelectFragment selectFragment = new ObjectSelectFragment();
        selectFragment.setShowListener(views -> ((ListView) views[0]).setAdapter(new ObjectAdaptor(this, array)));
        selectFragment.setListener(new ObjectSelectFragment.ItemSelectListener() {
            @Override
            public void onItemSelected(int position) {
                SelectedObject.Builder builder = new SelectedObject.Builder();
                try {
                    selectedObject = builder.buildFromJsonObject(array.getJSONObject(position));
                    Toast.makeText(InteractiveObjectDetectActivity.this, selectedObject.getName(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void start() {

            }

            @Override
            public void end() {
                postFragment(next, nextId);
            }
        });
        return selectFragment;
    }

    public interface DetectedListener {
        void onDetected(String obj);
    }

    private Fragment getEntryDetectFragment() {
        EntryObjectDetectFragment objectDetectFragment = new EntryObjectDetectFragment();
        objectDetectFragment.setListener(new FragmentListener() {
            @Override
            public void start() {
                sendMessage("START_DETECT");
                objectDetectFragment.setAnswer(selectedObject.getName());
                detectedListener = objectDetectFragment.getListener();

            }

            @Override
            public void end() {

            }
        });
        return objectDetectFragment;
    }


    @Override
    protected void onConnect() {
        sendMessage("DB_GET_ALL");
    }

    @Override
    protected void onDisconnect() {
        sendMessage("PAUSE_DETECT");
//        sendMessage("STOP_DETECT");
    }
    @Override
    public void onStop() {
        sendMessage("PAUSE_DETECT");
//        sendMessage("STOP_DETECT");
        super.onStop();
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.inter_obj_detect_toolbar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_inter_obj_detect;
    }

    private void postFragment(Fragment fragment, String id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.inter_obj_detect_frame, fragment, id);
        fragmentTransaction.commit();
    }


}
