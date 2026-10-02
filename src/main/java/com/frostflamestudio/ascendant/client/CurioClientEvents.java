package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.registry.ModItems;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = AscendantMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CurioClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(ModItems.ARCHER_CLOAK.get(), ArcherCloakRenderer::new);
            CuriosRendererRegistry.register(ModItems.LEATHER_QUIVER.get(), LeatherQuiverRenderer::new);
            CuriosRendererRegistry.register(ModItems.HOLY_SEAL.get(), HolySealRenderer::new);
        });
    }
}
