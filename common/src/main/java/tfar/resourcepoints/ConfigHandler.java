package tfar.resourcepoints;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Map;

public class ConfigHandler {

    static final File file = new File("config/resourcepoints-values.json");


    static JsonObject createDefault() {
        JsonObject root = new JsonObject();
        root.addProperty("cobblestone",1);
        root.addProperty("oak_log",4);
        return root;
    }


    private static final Logger LOGGER = LogManager.getLogger();

    public static JsonObject read(Gson gson) {
        writeIfEmpty();

        Reader reader = null;
        try {
            reader = new FileReader(file);
            JsonReader jsonReader = new JsonReader(reader);
            // Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();
            LOGGER.info("Loading existing config");
            return gson.fromJson(jsonReader, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void load(JsonObject jsonObject) {
        ResourcePoints.RESOURCE_POINTS.clear();
        for (Map.Entry<String,JsonElement> entry : jsonObject.asMap().entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue().getAsInt();
            ResourceLocation location = new ResourceLocation(key);
           if (!BuiltInRegistries.ITEM.containsKey(location)) {
               LOGGER.warn("Skipping unregistered item {}",key);
               continue;
            }
           Item item = BuiltInRegistries.ITEM.get(location);
            ResourcePoints.RESOURCE_POINTS.put(item,value);
        }
    }

    public static void writeIfEmpty() {
        if (!file.exists()) {
            write(file,createDefault());
        }
    }

    public static void save() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Item,Integer> entry : ResourcePoints.RESOURCE_POINTS.object2IntEntrySet()) {
            jsonObject.addProperty(BuiltInRegistries.ITEM.getKey(entry.getKey()).toString(),entry.getValue());
        }


        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
            writer.setIndent("    ");
            gson.toJson(jsonObject, writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void write(File file,JsonObject jsonObject) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
            writer.setIndent("    ");
            gson.toJson(jsonObject, writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
