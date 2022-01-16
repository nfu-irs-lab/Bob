package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.hiwin.teacher_version_bob.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VocabularyInteractiveFragment extends StaticFragment {

    private int groupA_score = 0;
    private int groupB_score = 0;
    private int index=0;
    private int index_limit = 15;
    private Button[] btns;
    private TextView score,definition;
    private Context context;
    private JSONArray vocabularies;
    private JSONObject[] chosen;
    private FragmentListener listener;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vocabulary_interactive, container, false);

        Button btn1 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_1));
        btn1.setOnClickListener(onClickListener);

        Button btn2 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_2));
        btn2.setOnClickListener(onClickListener);

        Button btn3 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_3));
        btn3.setOnClickListener(onClickListener);

        Button btn4 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_4));
        btn4.setOnClickListener(onClickListener);

        btns = new Button[]{btn1, btn2, btn3, btn4};
        definition = ((TextView) root.findViewById(R.id.vocabulary_interactive_definition));
        score = ((TextView) root.findViewById(R.id.vocabulary_interactive_score));

        try {
            showProblem(chosen[0], generateOptions(chosen[0], vocabularies));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected View[] getViews() {
        return new View[0];
    }

    public void initialize(Context context, JSONArray vocabularies) throws JSONException {
        this.context = context;
        this.vocabularies = vocabularies;
        chosen = new JSONObject[index_limit];
        JSONArray rest_of_the_words = new JSONArray();
        for (int i = 0; i < vocabularies.length(); i++) {
            rest_of_the_words.put(vocabularies.get(i));
        }

        for (int i = 0; i < chosen.length; i++) {
            int chosen_index = (int) (Math.random() * rest_of_the_words.length());
            chosen[i] = rest_of_the_words.getJSONObject(chosen_index);
            rest_of_the_words.remove(chosen_index);
        }
    }

    private void showProblem(JSONObject correct_vocabulary, JSONObject[] options) throws JSONException {
        for (int i = 0; i < options.length; i++) {
            btns[i].setText(options[i].getString("name"));
        }
        definition.setText(correct_vocabulary.getString("definition"));
        score.setText("score:" + groupA_score);
    }

    private JSONObject[] generateOptions(JSONObject correct_vocabulary, JSONArray vocabularies) throws JSONException {
        String correct_name = correct_vocabulary.getString("name");
        JSONArray wrong_options = new JSONArray();
        for (int i = 0; i < vocabularies.length(); i++) {
            String wrong_name = vocabularies.getJSONObject(i).getString("name");
            if (!wrong_name.equals(correct_name))
                wrong_options.put(vocabularies.get(i));
        }

        JSONObject[] options = new JSONObject[4];
        int correct_index = (int) (Math.random() * 4);
        options[correct_index] = correct_vocabulary;

        for (int i = 0; i < options.length; i++) {
            if (i == correct_index)
                continue;

            int j = (int) (Math.random() * wrong_options.length());
            options[i] = wrong_options.getJSONObject(j);
            wrong_options.remove(j);
        }

        return options;
    }

    private final View.OnClickListener onClickListener= v -> {
        try {
            boolean correct=chosen[index].getString("name").equals(((Button)root.findViewById(v.getId())).getText().toString());
            if(correct){
                groupA_score++;
            }

            MediaPlayer mp = MediaPlayer.create(getContext(), correct ? R.raw.sound_correct : R.raw.sound_wrong);
            mp.start();

            if (index < index_limit -1 && index >= 0) {
                showProblem(chosen[++index],generateOptions(chosen[index],vocabularies));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    @Override
    public <L extends FragmentListener> void setListener(L listener) {
        this.listener = listener;
    }
}
