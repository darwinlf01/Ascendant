package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum PlayerClass {
    NONE,
    ADVENTURER;

    public static final StreamCodec<ByteBuf, PlayerClass> STREAM_CODEC =

        ByteBufCodecs.STRING_UTF8.map(
            PlayerClass::valueOf,
            PlayerClass::name
        );

    public Component getDisplayName() {
        return Component.translatable("player_class.ascendant." + name().toLowerCase());
    }
}
