package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(AscendantMod.MODID);

    public static final DeferredBlock<Block> MANA_CRYSTAL_BLOCK =
    BLOCKS.registerSimpleBlock(
            "mana_crystal_block",
            BlockBehaviour.Properties.of()
                .strength(3.0F, 6.0F)
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
    );
}
