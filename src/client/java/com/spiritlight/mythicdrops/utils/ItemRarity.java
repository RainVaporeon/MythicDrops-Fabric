package com.spiritlight.mythicdrops.utils;

import java.util.Arrays;

public enum ItemRarity {
    NORMAL,
    UNIQUE,
    RARE,
    SET,
    LEGENDARY,
    FABLED,
    MYTHIC,
    INGREDIENT,
    UNKNOWN;

    public static ItemRarity fromString(String str) {
        // Ingredient testing
        if("0123456789".contains(str)) return INGREDIENT;
        return Arrays.stream(values()).filter(s -> s.name().equalsIgnoreCase(str))
                .findAny().orElse(UNKNOWN);
    }
}
