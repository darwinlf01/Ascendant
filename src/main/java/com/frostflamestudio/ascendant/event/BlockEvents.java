package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.registry.ModBlocks;
import com.frostflamestudio.ascendant.system.MiningSystem;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class BlockEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        var player = event.getPlayer();
        if (player.level().isClientSide) {
            return;
        }
        var blockState = event.getState();
        boolean isPickaxeMineable = blockState.is(BlockTags.MINEABLE_WITH_PICKAXE);
        var block = blockState.getBlock();

        if (isPickaxeMineable) {
            MiningSystem.onPickaxeMine(player);
        }
        if (block == ModBlocks.MANA_CRYSTAL_BLOCK.get()) {
            player.sendSystemMessage(Component.translatable("message.ascendant.mana_crystal_block_broken"));
        }
    }
}
