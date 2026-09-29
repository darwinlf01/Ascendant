package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.item.LeatherQuiverItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

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
    
    public static final DeferredItem<SwordItem> WOODEN_STAFF =
        ITEMS.register("wooden_staff", () -> new SwordItem(
            Tiers.WOOD,
            new Item.Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(Tiers.WOOD, 2, -2.8f))
        ));

    public static final DeferredItem<SwordItem> STEEL_DAGGER =
        ITEMS.register("steel_dagger", () -> new SwordItem(
            Tiers.IRON,
            new Item.Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.6f))
        ));

    public static final DeferredItem<BowItem> WOODEN_BOW =
        ITEMS.register("wooden_bow", () -> new BowItem(
            new Item.Properties().durability(128)
        ));

    public static final DeferredItem<SwordItem> STEEL_LONGSWORD =
        ITEMS.register("steel_longsword", () -> new SwordItem(
            Tiers.IRON,
            new Item.Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.6f))
        ));

    public static final DeferredItem<ShieldItem> HEATER_SHIELD =
        ITEMS.register("heater_shield", () -> new ShieldItem(
            new Item.Properties().durability(168)
        ));

    public static final DeferredItem<SwordItem> STEEL_ARMING_SWORD =
        ITEMS.register("steel_arming_sword", () -> new SwordItem(
            Tiers.IRON,
            new Item.Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(Tiers.IRON, 2, -2.2f))
        ));
    
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

    public static final DeferredItem<ArmorItem> CASTER_ROBE =
        ITEMS.register("caster_robe", () -> new ArmorItem(
            ArmorMaterials.LEATHER,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5))
        ));
    
    public static final DeferredItem<ArmorItem> HEALER_ROBE =
        ITEMS.register("healer_robe", () -> new ArmorItem(
            ArmorMaterials.LEATHER,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5))
        ));

    public static final DeferredItem<ArmorItem> LIGHT_LEATHER =
        ITEMS.register("light_leather", () -> new ArmorItem(
            ArmorMaterials.LEATHER,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5))
        ));

    public static final DeferredItem<ArmorItem> RIVETED_LEATHER =
        ITEMS.register("riveted_leather", () -> new ArmorItem(
            ArmorMaterials.CHAIN,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))
        ));

    public static final DeferredItem<ArmorItem> IRON_MAIL =
        ITEMS.register("iron_mail", () -> new ArmorItem(
            ArmorMaterials.IRON,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))
        ));

    public static final DeferredItem<LeatherQuiverItem> LEATHER_QUIVER =
        ITEMS.register("leather_quiver", () -> new LeatherQuiverItem(
            new Item.Properties().stacksTo(1)
        ));
}
