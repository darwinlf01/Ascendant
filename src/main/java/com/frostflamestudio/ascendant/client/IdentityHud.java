package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class IdentityHud {
    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        var id = ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "identity_hud");
        event.registerAboveAll(id, IdentityHud::onRender);
    }

    public static void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (player == null) {
            return;
        }
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        guiGraphics.drawString(
            minecraft.font,
            Component.translatable(
                "message.ascendant.race",
                playerData.getRace().getDisplayName()
            ),
            8,
            8,
            0xFFFFFF
        );

        guiGraphics.drawString(
            minecraft.font,
            Component.translatable(
                "message.ascendant.player_class",
                playerData.getPlayerClass().getDisplayName()
            ),
            8,
            20,
            0xFFFFFF
        );

        guiGraphics.drawString(
            minecraft.font,
            Component.translatable(
                "message.ascendant.profession",
                playerData.getProfession().getDisplayName()
            ),
            8,
            32,
            0xFFFFFF
        );
    }
}
