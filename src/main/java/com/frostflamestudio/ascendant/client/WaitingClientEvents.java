package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.system.TutorialSystem;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class WaitingClientEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        var player = event.getEntity();

        if (!player.level().dimension().equals(TutorialSystem.WAITING)) {
            return;
        }

        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getAvatarLine() < 4) {
            return;
        }

        if (playerData.getPlayerClass() != PlayerClass.NONE) {
            return;
        }

        var state = player.level().getBlockState(event.getPos());
        var playerClass = TutorialSystem.classFromPad(state);

        if (playerClass == null) {
            return;
        }

        event.setCanceled(true);
        Minecraft.getInstance().setScreen(new ConfirmClassScreen(playerClass));
    }
}
