package com.example.hiwin.teacher_version_bob.data.concrete.face.parser;

import com.example.hiwin.teacher_version_bob.data.framework.face.DataFace;
import com.example.hiwin.teacher_version_bob.data.framework.face.parser.DataFaceParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONFaceParser implements DataFaceParser<JSONObject> {
    private final String language;

    public JSONFaceParser(String language) {
        this.language = language;
    }

    @Override
    public DataFace parse(JSONObject json) throws JSONException {

        JSONArray languages = json.getJSONArray("languages");

        JSONObject translated = null;
        for (int i = 0; i < languages.length(); i++) {
            if (languages.getJSONObject(i).get("code").equals(language))
                translated = languages.getJSONObject(i);
        }

        if (translated == null)
            throw new RuntimeException("code not found");

        return new DataFace(json.getString("name"), translated.getString("tr_name"), json.getString("sentence"), translated.getString("tr_sentence"));
    }
}
