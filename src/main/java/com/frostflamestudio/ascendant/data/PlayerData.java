package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerData implements INBTSerializable<CompoundTag> {
    private MiningData miningData;
    private Race race = Race.NONE;
    private PlayerClass playerClass = PlayerClass.NONE;
    private Profession profession = Profession.NONE;

    private int avatarLine = 0;
    private StatData statData;
    private SkillBarData skillBarData;

    public PlayerData (){
        this(new MiningData(), Profession.NONE, Race.NONE, PlayerClass.NONE, 0, new StatData(), new SkillBarData());
    }

    public PlayerData (MiningData miningData, Profession profession, Race race, PlayerClass playerClass, int avatarLine, StatData statData, SkillBarData skillBarData) {
        this.miningData = miningData;
        this.profession = profession;
        this.race = race;
        this.playerClass = playerClass;
        this.avatarLine = avatarLine;
        this.statData = statData;
        this.skillBarData = skillBarData;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();
        tag.put("mining", miningData.serializeNBT(provider));
        tag.putString("race", race.name());
        tag.putString("player_class", playerClass.name());
        tag.putString("profession", profession.name());
        tag.putInt("avatar_line", avatarLine);
        tag.put("stats", statData.serializeNBT(provider));
        tag.put("skill_bar", skillBarData.serializeNBT(provider));
        return tag;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt){
        miningData.deserializeNBT(provider, nbt.getCompound("mining"));
        var raceName = nbt.getString("race");
        var className = nbt.getString("player_class");
        var professionName = nbt.getString("profession");
        avatarLine = nbt.getInt("avatar_line");
        statData.deserializeNBT(provider, nbt.getCompound("stats"));

        if (nbt.contains("skill_bar")) {
            skillBarData.deserializeNBT(provider, nbt.getCompound("skill_bar"));
        }

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

    public static final StreamCodec<ByteBuf, PlayerData> STREAM_CODEC = StreamCodec.of(
        (buf, data) -> {
            MiningData.STREAM_CODEC.encode(buf, data.getMiningData());
            Profession.STREAM_CODEC.encode(buf, data.getProfession());
            Race.STREAM_CODEC.encode(buf, data.getRace());
            PlayerClass.STREAM_CODEC.encode(buf, data.getPlayerClass());
            ByteBufCodecs.INT.encode(buf, data.getAvatarLine());
            StatData.STREAM_CODEC.encode(buf, data.getStatData());
            SkillBarData.STREAM_CODEC.encode(buf, data.getSkillBarData());
        },
        buf -> new PlayerData(
            MiningData.STREAM_CODEC.decode(buf),
            Profession.STREAM_CODEC.decode(buf),
            Race.STREAM_CODEC.decode(buf),
            PlayerClass.STREAM_CODEC.decode(buf),
            ByteBufCodecs.INT.decode(buf),
            StatData.STREAM_CODEC.decode(buf),
            SkillBarData.STREAM_CODEC.decode(buf)
        )
    );

    //stats
    public StatData getStatData() {
        return statData;
    }

    public SkillBarData getSkillBarData() {
        return skillBarData;
    }

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

    //avatar
    public int getAvatarLine() {
        return this.avatarLine;
    }

    public void setAvatarLine(int line) {
        this.avatarLine = line;
    }
}
