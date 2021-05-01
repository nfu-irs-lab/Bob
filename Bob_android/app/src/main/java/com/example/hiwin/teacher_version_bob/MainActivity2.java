package com.example.hiwin.teacher_version_bob;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;
import static android.speech.tts.TextToSpeech.SUCCESS;

public class MainActivity2 extends AppCompatActivity {

    ObjectSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        speaker = ObjectSpeaker.init(this);
    }

    public void speak(View v) {
        String name = "car";
        String tr_name = "車";
        String sentence = "My car is beautiful.";
        String tr_sentence = "我的車很漂亮";
        speaker.speak(name, tr_name, sentence, tr_sentence);
    }
}