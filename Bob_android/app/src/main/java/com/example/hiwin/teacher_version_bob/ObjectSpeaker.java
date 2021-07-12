package com.example.hiwin.teacher_version_bob;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
import java.util.UUID;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class ObjectSpeaker {
    private static ObjectSpeaker speaker;

    public static ObjectSpeaker init(Context context) {
        if (speaker == null)
            speaker = new ObjectSpeaker(context);
        return speaker;
    }

    private final TextToSpeech tts;

    private ObjectSpeaker(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate(0.6f);
                }
            }
        });
    }

    public void speak(String name, String tr_name, String sentence, String tr_sentence) {

        if (!tts.isSpeaking()) {
            setLanguage(Locale.US);
            addTextToQueue(name);
            addDelayToQueue(100);
            addTextToQueue(name);
            addDelayToQueue(100);
            addTextToQueue(name);
            addDelayToQueue(600);
            spellVocabulary(name);

            setLanguage(Locale.TAIWAN);
            addTextToQueue(tr_name);

            setLanguage(Locale.US);
            addTextToQueue(sentence);
            addDelayToQueue(100);
            addTextToQueue(sentence);
            addDelayToQueue(100);
            addTextToQueue(sentence);
            addDelayToQueue(100);

            setLanguage(Locale.TAIWAN);
            addTextToQueue(tr_sentence);
        }
    }

    private void spellVocabulary(String vocabulary) {
        for (int i = 0; i < vocabulary.length(); i++) {
            addTextToQueue(vocabulary.charAt(i) + "");
            addDelayToQueue(600);
//            tts.speak(vocabulary.charAt(i) + "", QUEUE_ADD, null, "spv" + i);
//            tts.playSilentUtterance(600, QUEUE_ADD, "s1");
        }
    }
    private void addTextToQueue(String string){
        tts.speak(string, QUEUE_ADD, null, UUID.randomUUID().toString());
    }
    private void addDelayToQueue(int durationInMs){
        tts.playSilentUtterance(durationInMs, QUEUE_ADD, UUID.randomUUID().toString());
    }
    private void setLanguage(Locale locale){
        tts.setLanguage(locale);
    }
}
