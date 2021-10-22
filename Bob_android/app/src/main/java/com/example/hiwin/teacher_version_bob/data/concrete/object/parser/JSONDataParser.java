package com.example.hiwin.teacher_version_bob.data.concrete.object.parser;

import com.example.hiwin.teacher_version_bob.data.data.Data;
import com.example.hiwin.teacher_version_bob.data.framework.parser.DataParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDataParser implements DataParser<JSONObject, Data> {
    private final String language;

    public JSONDataParser(String language) {
        this.language = language;
    }

    @Override
    public Data parse(JSONObject json) throws JSONException {

        JSONArray languages = json.getJSONArray("languages");

        JSONObject translated = null;
        for (int i = 0; i < languages.length(); i++) {
            if (languages.getJSONObject(i).get("code").equals(language))
                translated = languages.getJSONObject(i);
        }

        if (translated == null)
            throw new RuntimeException("code not found");

        Data.Builder builder = new Data.Builder();
        builder.setName(json.getString("name"));
        builder.setSentence(json.getString("sentence"));
        builder.setTranslatedName(translated.getString("tr_name"));
        builder.setTranslatedSentence(translated.getString("tr_sentence"));
        builder.setFace(json.getString("face"));
        return builder.build();
    }
}
