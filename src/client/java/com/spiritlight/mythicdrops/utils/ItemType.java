package com.spiritlight.mythicdrops.utils;

import java.util.Arrays;

public enum ItemType {
    SPEAR,
    WAND,
    BOW,
    RELIK,
    DAGGER,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    INGREDIENT,
    UNKNOWN;

    public static ItemType fromString(String str) {
        return Arrays.stream(values()).filter(s -> s.name().equalsIgnoreCase(str))
                .findAny().orElse(UNKNOWN);
    }
}
