package com.frostflamestudio.ascendant.client;

import net.minecraft.network.chat.Component;

public enum MenuSection {
    STATUS,
    SKILLS,
    TITLES,
    BLESSING,
    BLOODLINE;

    public Component getDisplayName() {
        return Component.translatable("screen.ascendant.tab." + name().toLowerCase());
    }
}