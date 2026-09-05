package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.system.CharacterSystem;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();

        if (player.level().isClientSide){
            return;
        }

        player.sendSystemMessage(Component.translatable("message.ascendant.welcome"));
        AscendantMod.LOGGER.info("Player logged in: {}", player.getName().getString());

        CharacterSystem.applyLoginDefaults(player);
    }
}
