package exchangemage.base;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigLoader {
    private record resourcePaths(String root,
                                 String cards,
                                 String effects,
                                 String targetSelectors,
                                 String triggers) {}
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode config;

    private static void loadConfig() {
        try {
            config = mapper.readTree(ConfigLoader.class.getResourceAsStream("/config.json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.json due to the following error:\n"
                                       + e.getMessage(), e);
        }
    }

    public static resourcePaths getResourcePaths() {
        if (config == null)
            loadConfig();
        JsonNode resourcePaths = config.get("resourcePaths");
        return new resourcePaths(resourcePaths.get("root").asText(),
                                 resourcePaths.get("cards").asText(),
                                 resourcePaths.get("effects").asText(),
                                 resourcePaths.get("targetSelectors").asText(),
                                 resourcePaths.get("triggers").asText());
    }
}
