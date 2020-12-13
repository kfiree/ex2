package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GameGraph_Deserializer implements JsonDeserializer<DS_DWGraph> {

    @Override
    public DS_DWGraph deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException{
        DS_DWGraph g = new DS_DWGraph();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject nodeJsonObj = jsonObject.get("Nodes").getAsJsonObject();
        JsonObject edgeJsonObj = jsonObject.get("Edges").getAsJsonObject();

        for (Map.Entry<String, JsonElement> setN : nodeJsonObj.entrySet()) {
            JsonObject node_dataObj = setN.getValue().getAsJsonObject();
            int key = node_dataObj.get("id").getAsInt();
            node_data n = new nodeData(key);
            g.addNode(n);
        }
        for (Map.Entry<String, JsonElement> setE : edgeJsonObj.entrySet()) {
            JsonObject edge_data = setE.getValue().getAsJsonObject();
            int src = edge_data.get("src").getAsInt();
            int dest = edge_data.get("dest").getAsInt();
            double w = edge_data.get("w").getAsDouble();
            g.connect(src, dest, w);
        }
        return g;
    }
}

