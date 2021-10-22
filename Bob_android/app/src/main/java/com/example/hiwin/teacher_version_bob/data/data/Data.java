package com.example.hiwin.teacher_version_bob.data.data;

public class Data {

    public static class Builder{
        private String name;
        private String translated_name;
        private String sentence;
        private String translated_sentence;
        private Face face;

        public void setName(String name) {
            this.name = name;
        }

        public void setTranslatedName(String translated_name) {
            this.translated_name = translated_name;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public void setTranslatedSentence(String translated_sentence) {
            this.translated_sentence = translated_sentence;
        }

        public void setFace(String face_string) {
            this.face = Face.valueOf(face_string);
        }

        public Data build(){
            return new Data(name,translated_name,sentence,translated_sentence,face);
        }
    }

    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;
    private final Face face;

    private Data(String name, String translated_name, String sentence, String translated_sentence, Face face) {
        this.name = name;
        this.translated_name = translated_name;
        this.sentence = sentence;
        this.translated_sentence = translated_sentence;
        this.face = face;
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

    public Face getFace() {
        return face;
    }
}
