package com.frostflamestudio.ascendant.registry;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import com.frostflamestudio.ascendant.AscendantMod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
        DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, AscendantMod.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LIGHT_LEATHER =
        register("light_leather", 3, 5, () -> Ingredient.of(Items.LEATHER), SoundEvents.ARMOR_EQUIP_LEATHER);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RIVETED_LEATHER =
        register("riveted_leather", 5, 15, () -> Ingredient.of(Items.LEATHER), SoundEvents.ARMOR_EQUIP_CHAIN);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IRON_MAIL =
        register("iron_mail", 6, 15, () -> Ingredient.of(Items.IRON_INGOT), SoundEvents.ARMOR_EQUIP_IRON);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CASTER_ROBE =
        register("caster_robe", 3, 5, () -> Ingredient.of(Items.LEATHER), SoundEvents.ARMOR_EQUIP_LEATHER);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HEALER_ROBE =
        register("healer_robe", 3, 5, () -> Ingredient.of(Items.LEATHER), SoundEvents.ARMOR_EQUIP_LEATHER);

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
        String name,
        int chestDefense,
        int enchantmentValue,
        Supplier<Ingredient> repair,
        net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> equipSound
    ) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
            defense(chestDefense),
            enchantmentValue,
            equipSound,
            repair,
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, name))),
            0.0F,
            0.0F
        ));
    }

    private static EnumMap<ArmorItem.Type, Integer> defense(int chest) {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            defense.put(type, type == ArmorItem.Type.CHESTPLATE ? chest : 0);
        }
        return defense;
    }
}
