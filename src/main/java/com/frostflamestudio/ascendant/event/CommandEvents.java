package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.data.Race;
import com.frostflamestudio.ascendant.system.CharacterSystem;
import com.frostflamestudio.ascendant.system.MiningSystem;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class CommandEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("ascendant")
                .executes(ctx -> forPlayer(ctx, player -> {
                    CharacterSystem.showIdentity(player);
                    return 1;
                }))
                .then(
                    Commands.literal("mining")
                        .executes(ctx -> forPlayer(ctx, player -> {
                            MiningSystem.showStatus(player, player);
                            return 1;
                        }))
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .requires(source -> source.hasPermission(2))
                                .executes(ctx -> forPlayer(ctx, viewer -> {
                                    var target = EntityArgument.getPlayer(ctx, "target");
                                    MiningSystem.showStatus(viewer, target);
                                    return 1;
                                }))
                        )
                )
                .then(
                    Commands.literal("race")
                        .requires(source -> source.hasPermission(2))
                        .then(
                            Commands.argument("value", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    addEnumSuggestions(builder, Race.values());
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> forPlayer(ctx, player ->
                                    CharacterSystem.setRace(player, StringArgumentType.getString(ctx, "value")) ? 1 : 0
                                ))
                        )
                )
                .then(
                    Commands.literal("class")
                        .requires(source -> source.hasPermission(2))
                        .then(
                            Commands.argument("value", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    addEnumSuggestions(builder, PlayerClass.values());
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> forPlayer(ctx, player ->
                                    CharacterSystem.setPlayerClass(player, StringArgumentType.getString(ctx, "value")) ? 1 : 0
                                ))
                        )
                )
        );
    }

    private static void addEnumSuggestions(SuggestionsBuilder builder, Enum<?>[] values) {
        for (var value : values) {
            builder.suggest(value.name().toLowerCase());
        }
    }

    private static int forPlayer(
        CommandContext<CommandSourceStack> ctx,
        PlayerAction action
    ) throws CommandSyntaxException {
        var player = ctx.getSource().getPlayer();
        if (player == null) {
            return 0;
        }

        return action.run(player);
    }

    @FunctionalInterface
    private interface PlayerAction {
        int run(ServerPlayer player) throws CommandSyntaxException;
    }
}
