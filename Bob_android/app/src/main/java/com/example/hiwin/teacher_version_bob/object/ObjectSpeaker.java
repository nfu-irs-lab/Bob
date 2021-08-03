package com.example.hiwin.teacher_version_bob.object;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.*;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class ObjectSpeaker {
    public interface SpeakerListener{
        void onSpeakComplete();
    }

    List<String> queue=new ArrayList<>();
    private final TextToSpeech tts;
    private SpeakerListener speakerListener;

    public ObjectSpeaker(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate(0.6f);
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                queue.remove(utteranceId);
                if(queue.isEmpty()){
                    if(speakerListener!=null)
                        speakerListener.onSpeakComplete();
                }
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    public void speak(DataObject object) {

        if (!tts.isSpeaking()) {
            setLanguage(Locale.US);
            addTextToQueue(object.getName());
            addDelayToQueue(100);
            addTextToQueue(object.getName());
            addDelayToQueue(100);
            addTextToQueue(object.getName());
            addDelayToQueue(600);
            spellVocabulary(object.getName());

            setLanguage(Locale.TAIWAN);
            addTextToQueue(object.getTranslatedName());

            setLanguage(Locale.US);
            addTextToQueue(object.getSentence());
            addDelayToQueue(100);
            addTextToQueue(object.getSentence());
            addDelayToQueue(100);
            addTextToQueue(object.getSentence());
            addDelayToQueue(100);

            setLanguage(Locale.TAIWAN);
            addTextToQueue(object.getTranslatedSentence());
        }
    }

    private void spellVocabulary(String vocabulary) {
        for (int i = 0; i < vocabulary.length(); i++) {
            addTextToQueue(vocabulary.charAt(i) + "");
            addDelayToQueue(600);
        }
    }
    private void addTextToQueue(String string){
        String id=UUID.randomUUID().toString();
        tts.speak(string, QUEUE_ADD, null, id);
        queue.add(id);
    }

    private void addDelayToQueue(int durationInMs){
        String id=UUID.randomUUID().toString();
        tts.playSilentUtterance(durationInMs, QUEUE_ADD,id);
        queue.add(id);
    }
    private void setLanguage(Locale locale){
        tts.setLanguage(locale);
    }

    public void setSpeakerListener(SpeakerListener speakerListener) {
        this.speakerListener = speakerListener;
    }

}
