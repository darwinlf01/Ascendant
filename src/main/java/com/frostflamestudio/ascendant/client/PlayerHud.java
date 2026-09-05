package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.Profession;
import com.frostflamestudio.ascendant.data.MiningData;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class PlayerHud {

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        var id = ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "player_hud");
        event.registerAboveAll(id, PlayerHud::onRender);
    }

    public static void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (player == null) {
            return;
        }
        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        int count = playerData.getMiningActions();

        if (playerData.getProfession() == Profession.MINING) {
            guiGraphics.drawString(
                minecraft.font,
                Component.translatable(
                    "message.ascendant.mining_level_xp", 
                    playerData.getMiningXpIntoLevel(),
                    MiningData.DISCOVERY_THRESHOLD
                ),
                8,
                44,
                0xFFFFFF
            );
        }
        else {
            guiGraphics.drawString(
                minecraft.font,
                Component.translatable("message.ascendant.mining_actions", count),
                8,
                44,
                0xFFFFFF
            );
        }

        if (playerData.getProfession() == Profession.MINING) {
            guiGraphics.drawString(
                minecraft.font,
                Component.translatable(
                    "message.ascendant.mining_level",
                    playerData.getMiningLevel()
                ),
                8,
                56,
                0xFFFFFF
            );
        }
    }
}
