package com.frostflamestudio.ascendant.network;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.Skill;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UseSkillPayload(Skill skill) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseSkillPayload> TYPE =
        new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "use_skill")
        );
    
    public static final StreamCodec<ByteBuf, UseSkillPayload> STREAM_CODEC =
        StreamCodec.composite(
            Skill.STREAM_CODEC,
            UseSkillPayload::skill,
            UseSkillPayload::new
        );
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
