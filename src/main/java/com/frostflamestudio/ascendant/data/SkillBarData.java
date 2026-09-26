package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SkillBarData implements INBTSerializable<CompoundTag> {
    public static final int SLOT_COUNT = 5;

    private final String[] slots = new String[] { "", "", "", "", "" };

    public Skill get(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            return null;
        }
        if (slots[slot].isEmpty()) {
            return null;
        }
        return Skill.valueOf(slots[slot]);
    }

    public void set(int slot, Skill skill) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            return;
        }
        clear(skill);
        slots[slot] = skill == null ? "" : skill.name();
    }

    public void clear(Skill skill) {
        if (skill == null) {
            return;
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (skill.name().equals(slots[i])) {
                slots[i] = "";
            }
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();

        for (int i = 0; i < SLOT_COUNT; i++) {
            tag.putString("slot" + i, slots[i]);
        }

        return tag;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt){
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = nbt.getString("slot" + i);
        }
    }

    public static final StreamCodec<ByteBuf, SkillBarData> STREAM_CODEC = StreamCodec.of(
        (buf, data) -> {
            for (int i = 0; i < SLOT_COUNT; i++) {
                ByteBufCodecs.STRING_UTF8.encode(buf, data.slots[i]);
            }
        },
        buf -> {
            var data = new SkillBarData();
            for (int i = 0; i < SLOT_COUNT; i++) {
                data.slots[i] = ByteBufCodecs.STRING_UTF8.decode(buf);
            }
            return data;
        }
    );
}