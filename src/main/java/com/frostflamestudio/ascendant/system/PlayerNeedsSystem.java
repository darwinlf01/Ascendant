package com.frostflamestudio.ascendant.system;

import net.minecraft.world.entity.player.Player;

public class PlayerNeedsSystem {
    public static void tickFood(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        var food = player.getFoodData();
        food.setFoodLevel(20);
        food.setSaturation(0.0F);
        food.setExhaustion(0.0F);
    }
}