package com.example.hiwin.teacher_version_bob.data.framework.face;

public class DataFace {
    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;

    public DataFace(String name, String translated_name, String sentence, String translated_sentence) {
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


    @Override
    public String toString() {
        return "JObject{" +
                "name='" + name + '\'' +
                ", translated_name='" + translated_name + '\'' +
                ", sentence='" + sentence + '\'' +
                ", translated_sentence='" + translated_sentence + '\'' +
                '}';
    }
}
