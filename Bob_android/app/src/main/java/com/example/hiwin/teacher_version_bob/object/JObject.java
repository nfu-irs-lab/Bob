package com.example.hiwin.teacher_version_bob.object;

import com.example.hiwin.teacher_version_bob.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JObject {
    public static class JSONParser{
        public JObject parse(String str,String language) throws JSONException {
            JSONObject json=new JSONObject(str);
            JSONArray languages = json.getJSONArray("languages");
            final JSONObject translated = languages.getJSONObject(0);
            return new JObject(json.getString("name"),translated.getString("tr_name"),json.getString("sentence"),translated.getString("tr_sentence"));
        }

        public JObject parse(JSONObject json,String language) throws JSONException {
            JSONArray languages = json.getJSONArray("languages");
            final JSONObject translated = languages.getJSONObject(0);
            return new JObject(json.getString("name"),translated.getString("tr_name"),json.getString("sentence"),translated.getString("tr_sentence"));
        }
    }

    private final String name;
    private final String translated_name;
    private final String sentence;
    private final String translated_sentence;

    public JObject(String name, String translated_name, String sentence, String translated_sentence) {
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

    public int getDrawableId() {
        switch (name) {
            case "car":
                return R.drawable.object_car;
            case "knife":
                return R.drawable.object_knife;
            case "cake":
                return R.drawable.object_cake;
            case "bird":
                return R.drawable.object_bird;
            case "bowl":
                return R.drawable.object_bowl;
        }
        return -1;
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
