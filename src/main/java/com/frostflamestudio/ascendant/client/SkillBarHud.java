package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class SkillBarHud {
    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        var id = ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "skill_bar");
        event.registerAbove(VanillaGuiLayers.HOTBAR, id, SkillBarHud::onRender);
    }

    public static void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        if (minecraft.options.hideGui) {
            return;
        }

        int slots = 5;
        int size = 22;
        int gap = 3;

        int totalWidth = slots * size + (slots - 1) * gap;

        int x = (guiGraphics.guiWidth() - totalWidth) / 2;
        int y = guiGraphics.guiHeight() - 48;

        for (int i = 0; i < slots; i++) {
            int slotX = x + i * (size + gap);
            guiGraphics.fill(slotX, y, slotX + size, y + size, 0x90000000);

            int x2 = slotX + size;
            int y2 = y + size;
            int border = 0xA0C0A060;

            guiGraphics.fill(slotX, y, x2, y + 1, border);
            guiGraphics.fill(slotX, y2 - 1, x2, y2, border);
            guiGraphics.fill(slotX, y, slotX + 1, y2, border);
            guiGraphics.fill(x2 - 1, y, x2, y2, border);
        }
    }
}