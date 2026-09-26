package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.mojang.blaze3d.platform.InputConstants;
import com.frostflamestudio.ascendant.data.Skill;
import com.frostflamestudio.ascendant.network.UseSkillPayload;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class MenuClientEvents {
    public static final KeyMapping OPEN_MENU = new KeyMapping(
        "key.ascendant.menu",
        InputConstants.KEY_K,
        "key.categories.ascendant"
    );

    public static final KeyMapping USE_IDENTIFY = new KeyMapping(
        "key.ascendant.identify",
        InputConstants.KEY_R,
        "key.categories.ascendant"
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_MENU);
        event.register(USE_IDENTIFY);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) { 
        var minecraft = Minecraft.getInstance();
        if (minecraft.player == null) { 
            return; 
        }

        while (OPEN_MENU.consumeClick()) {
            if (minecraft.screen instanceof AscendantMenuScreen) {
                minecraft.screen.onClose();
            } else if (minecraft.screen == null) {
                minecraft.setScreen(new AscendantMenuScreen());
            }
        }

        while (USE_IDENTIFY.consumeClick()) {
            if (minecraft.screen == null) {
                PacketDistributor.sendToServer(new UseSkillPayload(Skill.IDENTIFY));
            }
        }
    }
}