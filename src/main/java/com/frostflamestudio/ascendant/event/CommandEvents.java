package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.system.CharacterSystem;
import com.frostflamestudio.ascendant.system.MiningSystem;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class CommandEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("ascendant")
                .executes(ctx -> {
                    var player = ctx.getSource().getPlayer();
                    if (player == null) {
                        return 0;
                    }

                    CharacterSystem.showIdentity(player);

                    return 1;
                })
                .then(
                    Commands.literal("mining")
                        .executes(ctx -> {
                            var player = ctx.getSource().getPlayer();
                            if (player == null) {
                                return 0;
                            }

                            MiningSystem.showStatus(player, player);

                            return 1;
                        })
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .requires(source -> source.hasPermission(2))
                                .executes( ctx -> {
                                    var viewer = ctx.getSource().getPlayer();
                                    if (viewer == null) {
                                        return 0;
                                    }
                                    var target = EntityArgument.getPlayer(ctx, "target");

                                    MiningSystem.showStatus(viewer, target);

                                    return 1;
                                })
                        )
                )
                .then(
                    Commands.literal("race")
                        .requires(source -> source.hasPermission(2))
                        .then(
                            Commands.argument("value", StringArgumentType.word())
                                .executes(ctx -> {
                                    var player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        return 0;
                                    }

                                    var value = StringArgumentType.getString(ctx, "value");

                                    return CharacterSystem.setRace(player, value) ? 1 : 0;
                                })
                        )
                )
        );
    }
}
