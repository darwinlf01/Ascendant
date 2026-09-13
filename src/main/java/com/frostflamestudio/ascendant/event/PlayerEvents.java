package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.system.CharacterSystem;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.system.TutorialSystem;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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

        CharacterSystem.applyDefaultRace(player);

        if (player instanceof ServerPlayer serverPlayer) {
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            if (playerData.getPlayerClass() == PlayerClass.NONE) {
                TutorialSystem.sendToWaiting(serverPlayer);
            }
        }

        CharacterSystem.remindIfUnset(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        TutorialSystem.onPlayerTick(event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!TutorialSystem.isAvatar(event.getTarget())) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (!event.getEntity().level().isClientSide) {
            TutorialSystem.talkToAvatar(event.getEntity());
        }
    }
}
