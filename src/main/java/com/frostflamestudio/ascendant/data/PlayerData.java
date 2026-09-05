package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerData implements INBTSerializable<CompoundTag> {
    private MiningData miningData;
    private Race race = Race.NONE;
    private PlayerClass playerClass = PlayerClass.NONE;
    private Profession profession = Profession.NONE;

    public PlayerData (){
        this(new MiningData(), Profession.NONE, Race.NONE, PlayerClass.NONE);
    }

    public PlayerData (MiningData miningData, Profession profession, Race race, PlayerClass playerClass) {
        this.miningData = miningData;
        this.profession = profession;
        this.race = race;
        this.playerClass = playerClass;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();
        tag.put("mining", miningData.serializeNBT(provider));
        tag.putString("race", race.name());
        tag.putString("player_class", playerClass.name());
        tag.putString("profession", profession.name());
        return tag;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt){
        miningData.deserializeNBT(provider, nbt.getCompound("mining"));
        var raceName = nbt.getString("race");
        var className = nbt.getString("player_class");
        var professionName = nbt.getString("profession");

        if (raceName.isEmpty()) {
            race = Race.NONE;
        } else {
            race = Race.valueOf(raceName);
        }

        if (className.isEmpty()) {
            playerClass = PlayerClass.NONE;
        } else {
            playerClass = PlayerClass.valueOf(className);
        }

        if (professionName.isEmpty()) {
            profession = Profession.NONE;
        } else {
            profession = Profession.valueOf(professionName);
        }

        if (profession == Profession.NONE && getMiningActions() >= MiningData.DISCOVERY_THRESHOLD) {
            profession = Profession.MINING;
        }
    }

    public static final StreamCodec<ByteBuf, PlayerData> STREAM_CODEC =
        StreamCodec.composite(
            MiningData.STREAM_CODEC,
            PlayerData::getMiningData,
            Profession.STREAM_CODEC,
            PlayerData::getProfession,
            Race.STREAM_CODEC,
            PlayerData::getRace,
            PlayerClass.STREAM_CODEC,
            PlayerData::getPlayerClass,
            PlayerData::new
        );

    //raza
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    //clase
    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    //profesion
    public Profession getProfession() {
        return profession;
    }

    public MiningData getMiningData() {
        return miningData;
    }

    public void incrementMiningActions() {
        miningData.incrementMiningActions();
    }

    public void discoverMining() {
        profession = Profession.MINING;
    }

    public int getMiningActions() {
        return miningData.getMiningActions();
    }

    public int getMiningLevel() {
        if (profession != Profession.MINING) {
            return 0;
        }

        var level = getMiningActions() / MiningData.DISCOVERY_THRESHOLD;

        return Math.min(level, MiningData.MAX_LEVEL);
    }

    public int getMiningXpIntoLevel() {
        if (profession != Profession.MINING) {
            return 0;
        }

        if ( getMiningLevel() >= MiningData.MAX_LEVEL) {
            return MiningData.DISCOVERY_THRESHOLD;
        }

        return getMiningActions() % MiningData.DISCOVERY_THRESHOLD;
    }
}
