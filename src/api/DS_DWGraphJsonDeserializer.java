package api;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

public class DS_DWGraphJsonDeserializer implements JsonDeserializer<DS_DWGraph> {

    @Override
    public DS_DWGraph deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
    {
        DS_DWGraph g = new DS_DWGraph();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject nodeJsonObj = jsonObject.get("Nodes").getAsJsonObject();
        JsonObject edgeJsonObj = jsonObject.get("Edges").getAsJsonObject();

        for (Entry<String, JsonElement> setN : nodeJsonObj.entrySet()) {
            JsonObject node_dataObj = setN.getValue().getAsJsonObject();
            int key = node_dataObj.get("id").getAsInt();
            node_data n = new nodeData(key);
            g.addNode(n);
        }
        for (Entry<String, JsonElement> setE : edgeJsonObj.entrySet()) {
            JsonObject edge_dataObj = setE.getValue().getAsJsonObject();
            int src = edge_dataObj.get("src").getAsInt();
            int dest = edge_dataObj.get("dest").getAsInt();
            double w = edge_dataObj.get("w").getAsDouble();
            g.connect(src,dest,w);
        }
        return g;
    }

}
