package com.frostflamestudio.ascendant.data;

import net.minecraft.network.chat.Component;

public enum StatType {
    STRENGTH,
    AGILITY,
    ENDURANCE,
    VITALITY,
    TOUGHNESS,
    WISDOM,
    INTELLIGENCE,
    PERCEPTION,
    WILLPOWER,
    FREE_POINTS;

    public Component getDisplayName() {
        return Component.translatable("stat.ascendant." + name().toLowerCase());
    }
}
