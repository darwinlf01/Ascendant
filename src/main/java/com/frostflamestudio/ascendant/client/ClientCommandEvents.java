package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class ClientCommandEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("character")
            .executes(ctx -> {
                Minecraft.getInstance().setScreen(new CharacterCreationScreen());
                return 1;
            })
        );
    }
}
