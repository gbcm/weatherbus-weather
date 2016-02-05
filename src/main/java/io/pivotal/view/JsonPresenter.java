package io.pivotal.view;

import com.cedarsoftware.util.io.JsonWriter;

import java.util.HashMap;
import java.util.Map;

public class JsonPresenter {

    public String toJson() {
        Map<String, Object> options = new HashMap<String, Object>() {{
            put(JsonWriter.TYPE, false);
        }};

        return JsonWriter.objectToJson(this, options);
    }
}
