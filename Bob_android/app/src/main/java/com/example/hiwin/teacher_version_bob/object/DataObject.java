package com.example.hiwin.teacher_version_bob.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataObject {
    public static class JSONParser{
        public DataObject parse(String str, String language) throws JSONException {
            JSONObject json=new JSONObject(str);
            JSONArray languages = json.getJSONArray("languages");

            JSONObject translated = null;
            for(int i=0;i<languages.length();i++){
                if(languages.getJSONObject(i).get("code").equals(language))
                    translated=languages.getJSONObject(i);
            }

            if(translated==null)
                throw new RuntimeException("code not found");

            return new DataObject(json.getString("name"),translated.getString("tr_name"),json.getString("sentence"),translated.getString("tr_sentence"));
        }

        public DataObject parse(JSONObject json, String language) throws JSONException {
            JSONArray languages = json.getJSONArray("languages");

            JSONObject translated = null;
            for(int i=0;i<languages.length();i++){
                if(languages.getJSONObject(i).get("code").equals(language))
                    translated=languages.getJSONObject(i);
            }

            if(translated==null)
                throw new RuntimeException("code not found");

            return new DataObject(json.getString("name"),translated.getString("tr_name"),json.getString("sentence"),translated.getString("tr_sentence"));
        }
    }

    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;

    public DataObject(String name, String translated_name, String sentence, String translated_sentence) {
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
