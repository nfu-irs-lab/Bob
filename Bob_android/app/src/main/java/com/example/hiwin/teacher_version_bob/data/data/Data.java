package com.example.hiwin.teacher_version_bob.data.data;

public class Data {

    public static class Builder{

        private int id;
        private String responseType;
        private String content;

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

        public void setId(int id) {
            this.id = id;
        }

        public void setResponseType(String responseType) {
            this.responseType = responseType;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Data build(){
            return new Data(id,responseType,content,name,translated_name,sentence,translated_sentence,face);
        }
    }

    private final int id;
    private final String responseType;
    private final String content;
    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;
    private final Face face;

    private Data(int id, String responseType, String content, String name, String translated_name, String sentence, String translated_sentence, Face face) {
        this.id = id;
        this.responseType = responseType;
        this.content = content;
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

    public int getId() {
        return id;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getContent() {
        return content;
    }
}
