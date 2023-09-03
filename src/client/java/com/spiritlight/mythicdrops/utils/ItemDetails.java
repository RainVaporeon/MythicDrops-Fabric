package com.spiritlight.mythicdrops.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ItemDetails {
    public static final ItemDetails UNKNOWN = new ItemDetails("Unknown Item", ItemRarity.UNKNOWN, ItemType.UNKNOWN);

    private final String name;
    private final ItemRarity rarity;
    private final ItemType type;

    private ItemDetails(String name, ItemRarity rarity, ItemType type) {
        this.name = name;
        this.rarity = rarity;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public ItemType getType() {
        return type;
    }

    public static ItemDetails fromJson(JsonElement element) {
        return fromJson((JsonObject) element);
    }

    public static ItemDetails fromJson(JsonObject json) {
        String name = json.get("name").getAsString();
        ItemRarity rarity = ItemRarity.fromString(json.get("tier").getAsString());

        ItemType type;
        if(json.get("type") == null) {
            // Test with an ingredient-specific type
            if(json.has("ingredientPositionModifiers")) {
                type = ItemType.INGREDIENT;
            } else {
                type = ItemType.UNKNOWN;
            }
        } else {
            type = ItemType.fromString(json.get("type").getAsString());
        }

        return new ItemDetails(name, rarity, type);
    }
}
