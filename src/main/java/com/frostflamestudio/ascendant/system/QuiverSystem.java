package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.item.LeatherQuiverItem;
import com.frostflamestudio.ascendant.registry.ModItems;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

public class QuiverSystem {
    public static void onArrowJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (!(arrow.getOwner() instanceof Player player)) {
            return;
        }

        ItemStack quiver = equippedQuiver(player);
        if (quiver == null || LeatherQuiverItem.getArrows(quiver) <= 0) {
            return;
        }

        LeatherQuiverItem.setArrows(quiver, LeatherQuiverItem.getArrows(quiver) - 1);
    }

    public static void supplyArrow(LivingGetProjectileEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        ItemStack quiver = equippedQuiver(player);
        if (quiver == null || LeatherQuiverItem.getArrows(quiver) <= 0) {
            return;
        }

        event.setProjectileItemStack(new ItemStack(Items.ARROW));
    }

    private static ItemStack equippedQuiver(Player player) {
        return top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player)
            .flatMap(inventory -> inventory.findFirstCurio(ModItems.LEATHER_QUIVER.get()))
            .map(slot -> slot.stack())
            .orElse(null);
    }

    public static boolean infuse(Player player, ItemStack quiver) {
        if (player.level().isClientSide()) {
            return false;
        }
    
        int arrows = LeatherQuiverItem.getArrows(quiver);
        int missing = LeatherQuiverItem.ARROW_CAPACITY - arrows;
        if (missing <= 0) {
            return false;
        }
    
        var stats = player.getData(ModAttachments.PLAYER_DATA).getStatData();
        int spent = Math.min(missing, stats.getMana());
        if (spent <= 0) {
            return false;
        }
    
        stats.setMana(stats.getMana() - spent);
        LeatherQuiverItem.setArrows(quiver, arrows + spent);
        player.syncData(ModAttachments.PLAYER_DATA);
        return true;
    }
}