package live.nerotv.masterbase.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.URLDecoder;

public class GsonConfig {

    private final Gson gson;
    private final File jsonFile;
    private final String path;

    public GsonConfig(File file) {
        this.gson = new Gson();
        this.jsonFile = file;
        if (!jsonFile.exists()) {
            createEmptyJsonFile();
        }
        try {
            this.path = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void createEmptyJsonFile() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JsonObject rootNode = new JsonObject();
            writer.write(gson.toJson(rootNode));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getJsonFile() {
        return jsonFile;
    }

    public String getPath() {
        return path;
    }

    public Gson getGson() {
        return gson;
    }

    public void set(String path, Object value) {
        try {
            JsonObject rootNode = JsonParser.parseReader(jsonFileReader()).getAsJsonObject();
            String[] parts = path.split("\\.");
            JsonObject currentNode = rootNode;

            for (int i = 0; i < parts.length - 1; i++) {
                if (!currentNode.has(parts[i]) || !currentNode.get(parts[i]).isJsonObject()) {
                    currentNode.add(parts[i], new JsonObject());
                }
                currentNode = currentNode.getAsJsonObject(parts[i]);
            }

            JsonElement valueElement = gson.toJsonTree(value);
            currentNode.add(parts[parts.length - 1], valueElement);

            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(gson.toJson(rootNode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object get(String path) {
        try {
            JsonObject rootNode = JsonParser.parseReader(jsonFileReader()).getAsJsonObject();
            String[] parts = path.split("\\.");
            JsonElement currentNode = rootNode;

            for (String part : parts) {
                if (!currentNode.isJsonObject() || !currentNode.getAsJsonObject().has(part)) {
                    return null;
                }
                currentNode = currentNode.getAsJsonObject().get(part);
            }

            return gson.fromJson(currentNode, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(String path) {
        try {
            JsonObject rootNode = JsonParser.parseReader(jsonFileReader()).getAsJsonObject();
            String[] parts = path.split("\\.");
            JsonObject currentNode = rootNode;

            for (int i = 0; i < parts.length - 1; i++) {
                if (!currentNode.isJsonObject() || !currentNode.getAsJsonObject().has(parts[i])) {
                    return;
                }
                currentNode = currentNode.getAsJsonObject(parts[i]);
            }

            currentNode.getAsJsonObject().remove(parts[parts.length - 1]);

            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(gson.toJson(rootNode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileReader jsonFileReader() throws FileNotFoundException {
        return new FileReader(jsonFile);
    }
}
