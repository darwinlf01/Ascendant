package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class MiningData implements INBTSerializable<CompoundTag> {
    public static final int DISCOVERY_THRESHOLD = 10;
    public static final int MAX_LEVEL = 10;
    private int miningActions = 0;

    public MiningData() {
        this(0);
    }
    
    public MiningData(int miningActions) {
        this.miningActions = miningActions;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();
        tag.putInt("mining_actions", miningActions);
        return tag;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt){
        miningActions = nbt.getInt("mining_actions");
    }

    public static final StreamCodec<ByteBuf, MiningData> STREAM_CODEC =
        ByteBufCodecs.INT.map(
            MiningData::new, 
            MiningData::getMiningActions
        );


    public void incrementMiningActions() {
        miningActions++;
    }

    public int getMiningActions() {
        return miningActions;
    }
}
