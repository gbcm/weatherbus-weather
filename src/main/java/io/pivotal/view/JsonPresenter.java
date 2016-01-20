package io.pivotal.view;

import com.cedarsoftware.util.io.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pivotal on 1/8/16.
 */
public class JsonPresenter {

    public String toJson() {
        Map options = new HashMap<String, Object>() {{
            put(JsonWriter.TYPE, false);
        }};

        return JsonWriter.objectToJson(this, options);
    }
}
