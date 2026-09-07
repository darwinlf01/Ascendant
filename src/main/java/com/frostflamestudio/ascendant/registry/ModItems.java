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
    
    public static final DeferredItem<Item> WOODEN_STAFF = 
        ITEMS.registerSimpleItem(
            "wooden_staff",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> STEEL_DAGGER = 
        ITEMS.registerSimpleItem(
            "steel_dagger",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> WOODEN_BOW = 
        ITEMS.registerSimpleItem(
            "wooden_bow",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> STEEL_LONGSWORD = 
        ITEMS.registerSimpleItem(
            "steel_longsword",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> HEATER_SHIELD = 
        ITEMS.registerSimpleItem(
            "heater_shield",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> STEEL_ARMING_SWORD = 
        ITEMS.registerSimpleItem(
            "steel_arming_sword",
            new Item.Properties().stacksTo(1)
        );
    
    public static final DeferredItem<Item> HOLY_SEAL = 
        ITEMS.registerSimpleItem(
            "holy_seal",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> ARCHER_CLOAK = 
        ITEMS.registerSimpleItem(
            "archer_cloak",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> CASTER_ROBE = 
        ITEMS.registerSimpleItem(
            "caster_robe",
            new Item.Properties().stacksTo(1)
        );
    
    public static final DeferredItem<Item> HEALER_ROBE = 
        ITEMS.registerSimpleItem(
            "healer_robe",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> LIGHT_LEATHER = 
        ITEMS.registerSimpleItem(
            "light_leather",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> RIVETED_LEATHER = 
        ITEMS.registerSimpleItem(
            "riveted_leather",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> IRON_MAIL = 
        ITEMS.registerSimpleItem(
            "iron_mail",
            new Item.Properties().stacksTo(1)
        );

    public static final DeferredItem<Item> LEATHER_QUIVER = 
        ITEMS.registerSimpleItem(
            "leather_quiver",
            new Item.Properties().stacksTo(1)
        );
}
