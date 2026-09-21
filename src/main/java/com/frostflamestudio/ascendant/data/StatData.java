package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class StatData implements INBTSerializable<CompoundTag> {
    private int strength = 0;
    private int agility = 0;
    private int endurance = 0;
    private int vitality = 0;
    private int toughness = 0;
    private int wisdom = 0;
    private int intelligence = 0;
    private int perception = 0;
    private int willpower = 0;
    private int freePoints = 0;

    public StatData() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public StatData(
        int strength,
        int agility,
        int endurance,
        int vitality,
        int toughness,
        int wisdom,
        int intelligence,
        int perception,
        int willpower,
        int freePoints
    ) {
        this.strength = strength;
        this.agility = agility;
        this.endurance = endurance;
        this.vitality = vitality;
        this.toughness = toughness;
        this.wisdom = wisdom;
        this.intelligence = intelligence;
        this.perception = perception;
        this.willpower = willpower;
        this.freePoints = freePoints;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("strength", strength);
        tag.putInt("agility", agility);
        tag.putInt("endurance", endurance);
        tag.putInt("vitality", vitality);
        tag.putInt("toughness", toughness);
        tag.putInt("wisdom", wisdom);
        tag.putInt("intelligence", intelligence);
        tag.putInt("perception", perception);
        tag.putInt("willpower", willpower);
        tag.putInt("free_points", freePoints);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        strength = nbt.getInt("strength");
        agility = nbt.getInt("agility");
        endurance = nbt.getInt("endurance");
        vitality = nbt.getInt("vitality");
        toughness = nbt.getInt("toughness");
        wisdom = nbt.getInt("wisdom");
        intelligence = nbt.getInt("intelligence");
        perception = nbt.getInt("perception");
        willpower = nbt.getInt("willpower");
        freePoints = nbt.getInt("free_points");
    }

    public static final StreamCodec<ByteBuf, StatData> STREAM_CODEC = StreamCodec.of(
        (buf, data) -> {
            buf.writeInt(data.getStrength());
            buf.writeInt(data.getAgility());
            buf.writeInt(data.getEndurance());
            buf.writeInt(data.getVitality());
            buf.writeInt(data.getToughness());
            buf.writeInt(data.getWisdom());
            buf.writeInt(data.getIntelligence());
            buf.writeInt(data.getPerception());
            buf.writeInt(data.getWillpower());
            buf.writeInt(data.getFreePoints());
        },
        buf -> new StatData(
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )
    );

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getVitality() {
        return vitality;
    }

    public int getToughness() {
        return toughness;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getPerception() {
        return perception;
    }

    public int getWillpower() {
        return willpower;
    }

    public int getFreePoints() {
        return freePoints;
    }

    public void set(StatType type, int value) { 
        switch(type) {
            case STRENGTH -> strength = value;
            case AGILITY -> agility = value;
            case ENDURANCE -> endurance = value;
            case VITALITY -> vitality = value;
            case TOUGHNESS -> toughness = value;
            case WISDOM -> wisdom = value;
            case INTELLIGENCE -> intelligence = value;
            case PERCEPTION -> perception = value;
            case WILLPOWER -> willpower = value;
            case FREE_POINTS -> freePoints = value;
        }
    }

    public int get(StatType type) {
        return switch(type) {
            case STRENGTH -> strength;
            case AGILITY -> agility;
            case ENDURANCE -> endurance;
            case VITALITY -> vitality;
            case TOUGHNESS -> toughness;
            case WISDOM -> wisdom;
            case INTELLIGENCE -> intelligence;
            case PERCEPTION -> perception;
            case WILLPOWER -> willpower;
            case FREE_POINTS -> freePoints;
        };
    }
}