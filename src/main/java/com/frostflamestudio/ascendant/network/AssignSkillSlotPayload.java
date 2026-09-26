package com.frostflamestudio.ascendant.network;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.Skill;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AssignSkillSlotPayload(Skill skill, int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AssignSkillSlotPayload> TYPE =
        new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "assign_skill_slot")
        );
    
    public static final StreamCodec<ByteBuf, AssignSkillSlotPayload> STREAM_CODEC =
        StreamCodec.composite(
            Skill.STREAM_CODEC,
            AssignSkillSlotPayload::skill,
            ByteBufCodecs.INT,
            AssignSkillSlotPayload::slot,
            AssignSkillSlotPayload::new
        );
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
