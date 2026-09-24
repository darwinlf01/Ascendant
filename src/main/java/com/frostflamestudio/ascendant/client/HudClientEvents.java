package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class HudClientEvents {
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        var name = event.getName();
        if (name.equals(VanillaGuiLayers.PLAYER_HEALTH)
            || name.equals(VanillaGuiLayers.FOOD_LEVEL)
            || name.equals(VanillaGuiLayers.EXPERIENCE_BAR)
            || name.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) {
            event.setCanceled(true);
        }
    }
}