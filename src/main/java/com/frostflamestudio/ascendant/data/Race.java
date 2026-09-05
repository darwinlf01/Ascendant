package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum Race {
    NONE,
    HUMAN;

    public static final StreamCodec<ByteBuf, Race> STREAM_CODEC =

        ByteBufCodecs.STRING_UTF8.map(
            Race::valueOf,
            Race::name
        );
    
    public Component getDisplayName() {
        return Component.translatable("race.ascendant." + name().toLowerCase());
    }
}
