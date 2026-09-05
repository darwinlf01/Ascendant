package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(AscendantMod.MODID);

    public static final DeferredItem<Item> MANA_CRYSTAL = 
        ITEMS.registerSimpleItem(
            "mana_crystal",
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)
        );

    public static final DeferredItem<BlockItem> MANA_CRYSTAL_BLOCK_ITEM =
        ITEMS.registerSimpleBlockItem(
            "mana_crystal_block",
            ModBlocks.MANA_CRYSTAL_BLOCK
        );
}
