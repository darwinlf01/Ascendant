package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.Profession;
import com.frostflamestudio.ascendant.data.MiningData;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class MiningSystem {
    public static void onPickaxeMine(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        if (playerData.getMiningLevel() >= MiningData.MAX_LEVEL) {
            return;
        }
        int previousLevel = playerData.getMiningLevel();
        playerData.incrementMiningActions();
        int currentLevel = playerData.getMiningLevel();
        
        if (playerData.getMiningActions() >= MiningData.DISCOVERY_THRESHOLD && playerData.getProfession() == Profession.NONE) {
            playerData.discoverMining();
            player.sendSystemMessage(Component.translatable("message.ascendant.mining_milestone"));
        }

        if (currentLevel > previousLevel && playerData.getProfession() == Profession.MINING) {
            player.sendSystemMessage(Component.translatable("message.ascendant.mining_level_up", currentLevel));
        }

        player.syncData(ModAttachments.PLAYER_DATA);
    }

    public static void showStatus(Player viewer, Player target) {
        var playerData = target.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getProfession() == Profession.MINING) {
            viewer.sendSystemMessage(
                Component.translatable(
                    "message.ascendant.mining_level_xp", 
                        playerData.getMiningXpIntoLevel(),
                        MiningData.DISCOVERY_THRESHOLD
            ));

            viewer.sendSystemMessage(
                Component.translatable(
                    "message.ascendant.mining_level", playerData.getMiningLevel()
            ));
        } else {
            viewer.sendSystemMessage(
                Component.translatable(
                    "message.ascendant.mining_actions", playerData.getMiningActions()
            ));
        }
    }
}
