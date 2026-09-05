package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.Race;
import com.frostflamestudio.ascendant.data.PlayerClass;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;



public class CharacterSystem {

    public static void showIdentity(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        player.sendSystemMessage(
            Component.translatable(
                "message.ascendant.race", playerData.getRace().getDisplayName()
        ));
        player.sendSystemMessage(
            Component.translatable(
                "message.ascendant.player_class", playerData.getPlayerClass().getDisplayName()
        ));
        player.sendSystemMessage(
            Component.translatable(
                "message.ascendant.profession", playerData.getProfession().getDisplayName()
        ));
    }

    public static boolean setRace(Player player, String value) {
        try{
            Race race = Race.valueOf(value.toUpperCase());
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            playerData.setRace(race);
            player.syncData(ModAttachments.PLAYER_DATA);
            return true;
        }
        catch (IllegalArgumentException e){
            player.sendSystemMessage(Component.literal("No valid option"));
            return false;
        }
    }

    public static boolean setPlayerClass(Player player, String value) {
        try{
            PlayerClass playerClass = PlayerClass.valueOf(value.toUpperCase());
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            playerData.setPlayerClass(playerClass);
            player.syncData(ModAttachments.PLAYER_DATA);
            return true;
        }
        catch (IllegalArgumentException e){
            player.sendSystemMessage(Component.literal("No valid option"));
            return false;
        }
    }

    public static void remindIfUnset(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getRace() == Race.NONE || playerData.getPlayerClass() == PlayerClass.NONE) {
            player.sendSystemMessage(Component.translatable("message.ascendant.choose_identity"));
        }
    }
}
