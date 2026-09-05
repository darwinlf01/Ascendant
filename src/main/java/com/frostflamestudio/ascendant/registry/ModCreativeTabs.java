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
        }).build());
}
