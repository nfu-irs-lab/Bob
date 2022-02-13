package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiwin.teacher_version_bob.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;
import java.util.Arrays;


public class VocabularyInteractiveFragment extends StaticFragment {

    private static final int group_count = 2;
    private int group = 0;

    private static final int index_limit = 15;
    private int index = 0;

    private static final int game_limit = 3;
    private int game = 0;


    private Button[] buttons;
    private TextView definition, hint;
    private ImageView face_img;
    private Context context;
    private JSONArray vocabularies;
    private JSONObject[] chosen;
    private View root;
    private AnswerListener answerListener;

    public interface AnswerListener extends FragmentListener {
        void onAnswerCorrect();

        void onAnswerIncorrect();
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
        face_img = root.findViewById(R.id.vocabulary_interactive_face);
        face_img.setVisibility(View.INVISIBLE);

        buttons = new Button[]{btn1, btn2, btn3, btn4};
        definition = root.findViewById(R.id.vocabulary_interactive_definition);

        hint = root.findViewById(R.id.vocabulary_interactive_hint);

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
        JSONArray fixVocabulary = new JSONArray();
        for (int i = 0; i < 15; i++) {
            fixVocabulary.put(vocabularies.get(i));
        }

        this.vocabularies = fixVocabulary;
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
        hint.setText(index + "/" + index_limit);
        for (int i = 0; i < options.length; i++) {
            buttons[i].setText(options[i].getString("name"));
            buttons[i].setEnabled(true);
        }

        definition.setText(correct_vocabulary.getString("definition"));
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

    private final View.OnClickListener onClickListener = v -> {
        try {
            String correct_name = chosen[index].getString("name");
            boolean correct = correct_name.equals(((Button) root.findViewById(v.getId())).getText().toString());

            GifDrawable drawable = new GifDrawable(context.getResources(), correct ? R.drawable.face_gif_happy : R.drawable.face_gif_sad);
            drawable.setLoopCount(1);
            face_img.setImageDrawable(drawable);
            face_img.setVisibility(View.VISIBLE);
            Arrays.stream(buttons).forEach(btn -> btn.setVisibility(View.INVISIBLE));
            definition.setVisibility(View.INVISIBLE);

            MediaPlayer player = MediaPlayer.create(getContext(), correct ? R.raw.sound_correct_2 : R.raw.sound_incorrect_2);

            Handler handler = new Handler();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(() -> {
                            face_img.setVisibility(View.INVISIBLE);
                            Arrays.stream(buttons).forEach(btn -> btn.setVisibility(View.VISIBLE));
                            definition.setVisibility(View.VISIBLE);
                            next(correct);
                        });
                    }).start();
                }
            });

            player.start();
            if (correct) {
                if (answerListener != null)
                    answerListener.onAnswerCorrect();
            } else if (answerListener != null) {
                answerListener.onAnswerIncorrect();
                v.setEnabled(false);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    };

    private void next(boolean correct) {
        if (correct) {
            if (index < index_limit - 1 && index >= 0) {
                try {
                    showProblem(chosen[++index], generateOptions(chosen[index]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (group < group_count - 1) {
                    group++;
                    index = 0;
                    startNew();
                } else {
                    if (game < game_limit - 1) {
                        game++;
                        group = 0;
                        index = 0;
                        startNew();
                    } else if (answerListener != null)
                        answerListener.end();
                }
            }

        }
    }

    @Override
    public <L extends FragmentListener> void setListener(L listener) {
        this.answerListener = (AnswerListener) listener;
    }
}
