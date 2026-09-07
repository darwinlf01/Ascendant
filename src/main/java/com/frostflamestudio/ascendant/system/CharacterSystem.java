package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.registry.ModItems;
import com.frostflamestudio.ascendant.data.Race;
import com.frostflamestudio.ascendant.data.PlayerClass;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



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
            player.sendSystemMessage(Component.translatable("message.ascendant.invalid_option"));
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
            player.sendSystemMessage(Component.translatable("message.ascendant.invalid_option"));
            return false;
        }
    }

    public static void remindIfUnset(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getPlayerClass() == PlayerClass.NONE) {
            player.sendSystemMessage(Component.translatable("message.ascendant.choose_identity"));
        }
    }

    public static void applyDefaultRace(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getRace() == Race.NONE) {
            playerData.setRace(Race.HUMAN);
            player.syncData(ModAttachments.PLAYER_DATA);
        }
    }

    public static void selectIdentity(Player player, PlayerClass playerClass) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);

        if (playerData.getPlayerClass() != PlayerClass.NONE) {
            player.sendSystemMessage(Component.translatable("message.ascendant.identity_locked"));
            return;
        }

        if (playerClass == PlayerClass.NONE) {
            player.sendSystemMessage(Component.translatable("message.ascendant.identity_required"));
            return;
        }

        playerData.setRace(Race.HUMAN);
        playerData.setPlayerClass(playerClass);
        player.syncData(ModAttachments.PLAYER_DATA);
        grantStarterKit(player, playerClass);
    }

    private static void grantStarterKit(Player player, PlayerClass playerClass) {
        switch (playerClass) {
            case LIGHT_WARRIOR:
                player.addItem(new ItemStack(ModItems.STEEL_DAGGER.get()));
                player.addItem(new ItemStack(ModItems.LIGHT_LEATHER.get()));
                break;
            case MEDIUM_WARRIOR:
                player.addItem(new ItemStack(ModItems.STEEL_LONGSWORD.get()));
                player.addItem(new ItemStack(ModItems.RIVETED_LEATHER.get()));
                break;
            case HEAVY_WARRIOR:
                player.addItem(new ItemStack(ModItems.HEATER_SHIELD.get()));
                player.addItem(new ItemStack(ModItems.STEEL_ARMING_SWORD.get()));
                player.addItem(new ItemStack(ModItems.IRON_MAIL.get()));
                break;
            case ARCHER:
                player.addItem(new ItemStack(ModItems.WOODEN_BOW.get()));
                player.addItem(new ItemStack(ModItems.STEEL_DAGGER.get()));
                player.addItem(new ItemStack(ModItems.ARCHER_CLOAK.get()));
                player.addItem(new ItemStack(ModItems.LEATHER_QUIVER.get()));
                break;
            case CASTER:
                player.addItem(new ItemStack(ModItems.WOODEN_STAFF.get()));
                player.addItem(new ItemStack(ModItems.CASTER_ROBE.get()));
                break;
            case HEALER:
                player.addItem(new ItemStack(ModItems.HOLY_SEAL.get()));
                player.addItem(new ItemStack(ModItems.HEALER_ROBE.get()));
                break;
            default:
                return;
        }
    }
}
