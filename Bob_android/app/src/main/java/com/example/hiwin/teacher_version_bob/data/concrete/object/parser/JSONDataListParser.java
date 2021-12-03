package com.example.hiwin.teacher_version_bob.data.concrete.object.parser;

import com.example.hiwin.teacher_version_bob.data.data.Data;
import com.example.hiwin.teacher_version_bob.data.framework.parser.DataParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JSONDataListParser implements DataParser<JSONArray, List<Data>> {
    private final String language;

    public JSONDataListParser(String language) {
        this.language = language;
    }

    @Override
    public List<Data> parse(JSONArray array) throws JSONException {
        List<Data> dataList = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            JSONArray languages = json.getJSONArray("languages");

            JSONObject translated = null;
            for (int j = 0; j < languages.length(); j++) {
                if (languages.getJSONObject(j).get("code").equals(language))
                    translated = languages.getJSONObject(j);
            }

            if (translated == null)
                throw new RuntimeException("code not found");

            Data.Builder builder = new Data.Builder();
            builder.setName(json.getString("name"));
            builder.setSentence(json.getString("sentence"));
            builder.setTranslatedName(translated.getString("tr_name"));
            builder.setTranslatedSentence(translated.getString("tr_sentence"));
            builder.setFace(json.getString("face"));
            dataList.add(builder.build());
        }
        return dataList;
    }
}
