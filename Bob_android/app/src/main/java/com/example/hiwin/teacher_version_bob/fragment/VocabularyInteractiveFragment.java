package com.example.hiwin.teacher_version_bob.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.Arrays;

import static java.lang.String.format;

public class VocabularyInteractiveFragment extends StaticFragment {

    private static final int group_count = 2;
    private int group = 0;
    private int[] group_scores;

    private static final int index_limit = 15;
    private int index = 0;

    private static final int game_limit = 3;
    private int game = 0;


    private Button[] buttons;
    private TextView score, definition;
    private Context context;
    private JSONArray vocabularies;
    private JSONObject[] chosen;
    private View root;
    private AnswerListener answerListener;

    public interface AnswerListener extends FragmentListener {
        void onAnswerCorrect();

        void onAnswerIncorrect();
    }

    public VocabularyInteractiveFragment() {
        initializeGroupScores(group_count);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vocabulary_interactive, container, false);

        Button btn1 = root.findViewById(R.id.vocabulary_interactive_answer_1);
        btn1.setOnClickListener(onClickListener);

        Button btn2 = root.findViewById(R.id.vocabulary_interactive_answer_2);
        btn2.setOnClickListener(onClickListener);

        Button btn3 = root.findViewById(R.id.vocabulary_interactive_answer_3);
        btn3.setOnClickListener(onClickListener);

        Button btn4 = root.findViewById(R.id.vocabulary_interactive_answer_4);
        btn4.setOnClickListener(onClickListener);

        buttons = new Button[]{btn1, btn2, btn3, btn4};
        definition = root.findViewById(R.id.vocabulary_interactive_definition);
        score = root.findViewById(R.id.vocabulary_interactive_score);

        startNew();
        return root;
    }

    private void startNew() {
        try {
            chosen = chooseVocabulary();
            showProblem(chosen[index], generateOptions(chosen[index]));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected View[] getViews() {
        return new View[0];
    }

    public void initialize(Context context, JSONArray vocabularies) throws JSONException {
        this.context = context;
        this.vocabularies = vocabularies;
    }

    private JSONObject[] chooseVocabulary() throws JSONException {
        JSONObject[] chosen = new JSONObject[index_limit];
        JSONArray rest_of_the_words = new JSONArray();
        for (int i = 0; i < vocabularies.length(); i++) {
            rest_of_the_words.put(vocabularies.get(i));
        }

        for (int i = 0; i < chosen.length; i++) {
            int chosen_index = (int) (Math.random() * rest_of_the_words.length());
            chosen[i] = rest_of_the_words.getJSONObject(chosen_index);
            rest_of_the_words.remove(chosen_index);
        }
        return chosen;
    }


    private void showProblem(JSONObject correct_vocabulary, JSONObject[] options) throws JSONException {
        for (int i = 0; i < options.length; i++) {
            buttons[i].setText(options[i].getString("name"));
            buttons[i].setEnabled(true);
        }

        definition.setText(correct_vocabulary.getString("definition"));
        score.setText(group_scores[group] + "/" + (index + 1) + "/" + index_limit);
    }

    private JSONObject[] generateOptions(JSONObject correct_vocabulary) throws JSONException {
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

    boolean isTeaching = false;
    private final View.OnClickListener onClickListener = v -> {
        try {
            String correct_name = chosen[index].getString("name");
            boolean correct = correct_name.equals(((Button) root.findViewById(v.getId())).getText().toString());
            if (!isTeaching)
                MediaPlayer.create(getContext(), correct ? R.raw.sound_correct : R.raw.sound_wrong).start();

            if (correct) {
                if (answerListener != null)
                    answerListener.onAnswerCorrect();
                group_scores[group]++;
                isTeaching = false;
//                MediaPlayer.create(getContext(), getResourceIDByString(chosen[index].getString("audio"), "raw")).start();
            } else {
                if (answerListener != null)
                    answerListener.onAnswerIncorrect();
                group_scores[group]--;
                Arrays.stream(buttons).forEach(button -> button.setEnabled(button.getText().toString().equals(correct_name)));
                MediaPlayer.create(getContext(), getResourceIDByString(chosen[index].getString("audio"), "raw")).start();
                definition.setText("The answer is: " + correct_name);
                isTeaching = true;
                return;
            }

            if (index < index_limit - 1 && index >= 0) {
                showProblem(chosen[++index], generateOptions(chosen[index]));
            } else {
                if (group < group_scores.length - 1) {
                    group++;
                    index = 0;
                    startNew();
                } else {
//                    Toast.makeText(context, group_scores[0] + "/" + group_scores[1], Toast.LENGTH_SHORT).show();

                    initializeGroupScores(group_count);
                    if (game < game_limit - 1) {
                        showResult(null);
                        game++;
                        group = 0;
                        index = 0;
                        startNew();
                    } else
                        showResult(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (answerListener != null)
                                    answerListener.end();
                            }
                        });
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private void showResult(DialogInterface.OnCancelListener onCancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Result");
        int max_index = 0;
        int max = group_scores[max_index];
        for (int i = 1; i < group_scores.length; i++) {
            if (max < group_scores[i]) {
                max = group_scores[i];
                max_index = i;
            } else if (max == group_scores[i]) {
                max_index = -1;
            }
        }
        if (max_index != -1) {
            char g = (char) (0x41 + max_index);
            builder.setMessage("group " + g + " is winner.");
        } else {
            builder.setMessage("Tie.");
        }
        builder.setOnCancelListener(onCancelListener);
        builder.show();
    }

    @Override
    public <L extends FragmentListener> void setListener(L listener) {
        this.answerListener = (AnswerListener) listener;
    }

    private void initializeGroupScores(int size) {
        group_scores = new int[size];
    }


    private int getResourceIDByString(String resName, String type) {
        return context.getApplicationContext().getResources()
                .getIdentifier(resName
                        , type
                        , context.getPackageName());
    }
}
