package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum Profession {
    NONE,
    MINING;

    public static final StreamCodec<ByteBuf, Profession> STREAM_CODEC =

        ByteBufCodecs.STRING_UTF8.map(
            Profession::valueOf,
            Profession::name
        );
    
    public Component getDisplayName() {
        return Component.translatable("profession.ascendant." + name().toLowerCase());
    }
}
