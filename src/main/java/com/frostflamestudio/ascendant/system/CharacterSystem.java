package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.Race;
import com.frostflamestudio.ascendant.data.PlayerClass;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;



public class CharacterSystem {
    public static void applyLoginDefaults(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        boolean changed = false;

        if (playerData.getRace() == Race.NONE) {
            playerData.setRace(Race.HUMAN);
            changed = true;
        }

        if (playerData.getPlayerClass() == PlayerClass.NONE) {
            playerData.setPlayerClass(PlayerClass.ADVENTURER);
            changed = true;
        }

        if(changed) {
            player.syncData(ModAttachments.PLAYER_DATA);
        }
    }

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
        catch (Exception e){
            player.sendSystemMessage(Component.literal("No valid option"));
            return false;
        }


    }
}
