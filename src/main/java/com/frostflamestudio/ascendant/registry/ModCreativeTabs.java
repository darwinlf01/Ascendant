package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscendantMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASCENDANT_TAB = TABS.register("ascendant_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.ascendant")) //The language key for the title of your CreativeModeTab
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ModItems.MANA_CRYSTAL.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(ModItems.MANA_CRYSTAL.get());
            output.accept(ModItems.MANA_CRYSTAL_BLOCK_ITEM.get());
            output.accept(ModItems.WOODEN_STAFF.get());
            output.accept(ModItems.STEEL_DAGGER.get());
            output.accept(ModItems.WOODEN_BOW.get());
            output.accept(ModItems.STEEL_LONGSWORD.get());
            output.accept(ModItems.HEATER_SHIELD.get());
            output.accept(ModItems.STEEL_ARMING_SWORD.get());
            output.accept(ModItems.HOLY_SEAL.get());
            output.accept(ModItems.ARCHER_CLOAK.get());
            output.accept(ModItems.CASTER_ROBE.get());
            output.accept(ModItems.HEALER_ROBE.get());
            output.accept(ModItems.LIGHT_LEATHER.get());
            output.accept(ModItems.RIVETED_LEATHER.get());
            output.accept(ModItems.IRON_MAIL.get());
            output.accept(ModItems.LEATHER_QUIVER.get());
        }).build());
}
