package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum PlayerClass {
    NONE,
    LIGHT_WARRIOR,
    MEDIUM_WARRIOR,
    HEAVY_WARRIOR,
    ARCHER,
    CASTER,
    HEALER;

    public static final StreamCodec<ByteBuf, PlayerClass> STREAM_CODEC =

        ByteBufCodecs.STRING_UTF8.map(
            PlayerClass::valueOf,
            PlayerClass::name
        );

    public Component getDisplayName() {
        return Component.translatable("player_class.ascendant." + name().toLowerCase());
    }

    public static PlayerClass[] playable() {
        return new PlayerClass[] {
            LIGHT_WARRIOR, 
            MEDIUM_WARRIOR, 
            HEAVY_WARRIOR, 
            ARCHER, 
            CASTER, 
            HEALER
        };
    }
}
