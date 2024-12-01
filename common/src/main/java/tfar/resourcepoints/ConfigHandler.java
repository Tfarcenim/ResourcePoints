package tfar.resourcepoints;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.resourcepoints.platform.Services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ConfigHandler {

    static final File file = new File("config/resourcepoints-values.json");


    static JsonObject createDefault() {
        JsonObject root = new JsonObject();

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
            write(file);
        }
    }

    public static void write(File file) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
            writer.setIndent("    ");
            gson.toJson(createDefault(), writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static String getName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    public static Item getItem(String s) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(s));
    }

    public static String getName(Holder<Attribute> attribute) {
        return attribute.getRegisteredName();
    }

    public static Holder<Attribute> getAttribute(String s) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(s)).orElseThrow();
    }

}
