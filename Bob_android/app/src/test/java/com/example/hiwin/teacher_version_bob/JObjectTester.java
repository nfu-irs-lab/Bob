package com.example.hiwin.teacher_version_bob;

import com.example.hiwin.teacher_version_bob.object.JObject;
import org.json.JSONException;
import org.junit.Test;

public class JObjectTester {

    @Test
    public void A() throws JSONException {
        String json_str="{\n" +
                "  \"name\": \"Car\",\n" +
                "  \"sentence\": \"Carrrrr.\",\n" +
                "  \"languages\": [\n" +
                "    {\n" +
                "      \"tr_name\": \"車\",\n" +
                "      \"tr_sentence\": \"車車\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";


        JObject.JSONParser parser=new JObject.JSONParser();
        JObject o=parser.parse(json_str,"zhTw");

        System.out.println(o.toString());
    }
}
