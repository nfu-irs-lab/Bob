package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.activity.StoryActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VocabularyFragment extends StaticFragment {
    private View root;
    private int index = 0;
    private ImageView image;
    private TextView name_text, translated_text, definition_text;
    private Button action;
    private Context context;
    private JSONArray vocabularies;
    private MediaPlayer player;
    private ActionListener listener;
    private boolean hasAction;

    public interface ActionListener extends FragmentListener {
        void onAction(String cmd);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        image = root.findViewById(R.id.vocabulary_interactive_imageview);
        name_text = root.findViewById(R.id.vocabulary_name);
        translated_text = root.findViewById(R.id.vocabulary_translated);
        definition_text = root.findViewById(R.id.vocabulary_definition);

        (root.findViewById(R.id.vocabulary_previous)).setOnClickListener(onClickListener);
        (root.findViewById(R.id.vocabulary_speak)).setOnClickListener(onClickListener);
        (root.findViewById(R.id.vocabulary_next)).setOnClickListener(onClickListener);
        (root.findViewById(R.id.vocabulary_next_session)).setOnClickListener(onClickListener);
        action=(root.findViewById(R.id.vocabulary_do_action));
        action.setOnClickListener(onClickListener);

        try {
            show(vocabularies.getJSONObject(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected View[] getViews() {
        return new View[0];
    }

    public void initialize(Context context, JSONArray vocabularies) {
        this.context = context;
        this.vocabularies = vocabularies;
    }

    private void show(JSONObject vocabulary) throws JSONException {
        final int drawable_id = getResourceIDByString(vocabulary.getString("image"), "raw");
        final Drawable drawable = drawable_id <= 0 ? null : context.getDrawable(drawable_id);
        final int audio_id = getResourceIDByString(vocabulary.getString("audio"), "raw");
        String name = vocabulary.getString("name");
        String translated = vocabulary.getString("translated");
        String definition = vocabulary.getString("definition");
        String part_of_speech = vocabulary.getString("part_of_speech");
        hasAction=vocabularies.getJSONObject(index).has("action");
        action.setEnabled(hasAction);

        player = MediaPlayer.create(context, audio_id);
        player.start();

        image.setImageDrawable(drawable);
        name_text.setText(name + " (" + part_of_speech + ".)");
        translated_text.setText(translated);
        definition_text.setText(definition);

    }


    private int getResourceIDByString(String resName, String type) {
        return context.getApplicationContext().getResources()
                .getIdentifier(resName
                        , type
                        , context.getPackageName());
    }

    private final View.OnClickListener onClickListener = v -> {
        try {
            if (v.getId() == R.id.vocabulary_previous) {
                if (index < vocabularies.length() && index > 0) {
                    player.stop();
                    player.release();
                    show(vocabularies.getJSONObject(--index));
                }

            } else if (v.getId() == R.id.vocabulary_speak) {
                player.seekTo(0);
                player.start();
            } else if (v.getId() == R.id.vocabulary_next) {
                if (index < vocabularies.length() - 1 && index >= 0) {
                    player.stop();
                    player.release();
                    show(vocabularies.getJSONObject(++index));
                }
            } else if (v.getId() == R.id.vocabulary_next_session) {
                player.stop();
                player.release();
                if (listener != null)
                    listener.end();
            }else if(v.getId()==R.id.vocabulary_do_action){
                if (listener != null)
                    listener.onAction("DO_ACTION "+vocabularies.getJSONObject(index).getString("action"));
            } else
                throw new IllegalStateException();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public <L extends FragmentListener> void setListener(L listener) {
        this.listener = (ActionListener) listener;
    }

}
