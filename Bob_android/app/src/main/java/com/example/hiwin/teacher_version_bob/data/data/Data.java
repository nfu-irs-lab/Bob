package com.example.hiwin.teacher_version_bob.data.data;

public class Data {

    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;

    public Data(String name, String translated_name, String sentence, String translated_sentence) {
        this.name = name;
        this.translated_name = translated_name;
        this.sentence = sentence;
        this.translated_sentence = translated_sentence;
    }

    public String getName() {
        return name;
    }

    public String getTranslatedName() {
        return translated_name;
    }

    public String getSentence() {
        return sentence;
    }

    public String getTranslatedSentence() {
        return translated_sentence;
    }

}
