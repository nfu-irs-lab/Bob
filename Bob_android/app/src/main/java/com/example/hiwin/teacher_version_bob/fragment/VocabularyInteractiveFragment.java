package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class VocabularyInteractiveFragment extends StaticFragment {

    private int groupA_score = 0;
    private int groupB_score = 0;
    private int counter = 0;
    private int counter_limit = 10;
    private ImageView image;
    private Button[] btns;
    private TextView score;
    private Context context;
    private JSONArray vocabularies;
    private JSONObject[] chosen;

    public abstract static class AnswerListener implements FragmentListener {
        public abstract void onAnswerCorrect(String selected);

        public abstract void onAnswerIncorrect(String selected, String correct);
    }

    private AnswerListener listener;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vocabulary_interactive, container, false);

        image = ((ImageView) root.findViewById(R.id.vocabulary_interactive_imageview));
        Button btn1 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_1));
        btn1.setOnClickListener(onClickListener);

        Button btn2 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_2));
        btn2.setOnClickListener(onClickListener);

        Button btn3 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_3));
        btn3.setOnClickListener(onClickListener);

        Button btn4 = ((Button) root.findViewById(R.id.vocabulary_interactive_answer_4));
        btn4.setOnClickListener(onClickListener);
        btns = new Button[]{btn1, btn2, btn3, btn4};

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
        chosen = new JSONObject[counter_limit];
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

        final int drawable_id = getResourceIDByString(correct_vocabulary.getString("image"), "raw");
        image.setImageDrawable(drawable_id <= 0 ? null : context.getDrawable(drawable_id));
        score.setText("score:" + groupA_score);
    }

    private JSONObject[] generateOptions(JSONObject correct_vocabulary, JSONArray vocabularies) throws JSONException {
        String correct_name = correct_vocabulary.getString("name");
        JSONArray wrong_options = new JSONArray();
        for (int i = 0; i < vocabularies.length(); i++) {
            String wrong_name = vocabularies.getJSONObject(i).getString("name");
            if (!wrong_name.equals(correct_name))
                wrong_options.put(i, vocabularies.get(i));
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

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            counter++;
            if (counter >= counter_limit - 1) {
                Toast.makeText(context, "complete", Toast.LENGTH_SHORT).show();
            } else {
                String selected = ((Button) root.findViewById(v.getId())).getText().toString();
                try {
                    if (chosen[counter - 1].getString("name").equals(selected)) {
                        groupA_score++;
                    }
                    showProblem(chosen[counter], generateOptions(chosen[counter], vocabularies));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    public <L extends FragmentListener> void setListener(L listener) {
        this.listener = (AnswerListener) listener;
    }

    private int getResourceIDByString(String resName, String type) {
        return context.getApplicationContext().getResources()
                .getIdentifier(resName
                        , type
                        , context.getPackageName());
    }
}
