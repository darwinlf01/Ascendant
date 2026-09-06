package com.frostflamestudio.ascendant.network;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.data.Race;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SelectIdentityPayload(Race race, PlayerClass playerClass) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SelectIdentityPayload> TYPE =
        new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "select_identity")
        );
    
    public static final StreamCodec<ByteBuf, SelectIdentityPayload> STREAM_CODEC =
        StreamCodec.composite(
            Race.STREAM_CODEC,
            SelectIdentityPayload::race,
            PlayerClass.STREAM_CODEC,
            SelectIdentityPayload::playerClass,
            SelectIdentityPayload::new
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
