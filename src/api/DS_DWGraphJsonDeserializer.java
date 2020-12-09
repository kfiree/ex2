package api;
import com.google.gson.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import java.lang.reflect.Type;

public class DS_DWGraphJsonDeserializer implements JsonDeserializer<DS_DWGraph> {

    @Override
    public DS_DWGraph deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
    {
        DS_DWGraph g = new DS_DWGraph();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject nodeJsonObj = jsonObject.get("Nodes").getAsJsonObject();
        JsonObject edgeJsonObj = jsonObject.get("Edges").getAsJsonObject();

        return g;
    }
}
