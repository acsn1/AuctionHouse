package org.acsn1.auctionhouse.util;

import com.google.gson.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    private String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringToItem(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(string);
        } catch (Exception ignored) {}
        return config.getItemStack("item", null);
    }


    @Override
    public JsonElement serialize(ItemStack src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", itemToString(src));
        return obj;
    }

    @Override
    public ItemStack deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String item = obj.get("item").getAsString();
        return stringToItem(item);
    }




}
