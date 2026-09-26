package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.Skill;
import com.frostflamestudio.ascendant.data.SkillBarData;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

public class SkillSystem {
    public static void assignSlot(Player player, Skill skill, int slot) {
        if (slot < 0 || slot >= SkillBarData.SLOT_COUNT) {
            return;
        }

        if (!skill.goesOnBar()) {
            return;
        }

        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        if (!skill.isKnownBy(playerData)) {
            return;
        }

        playerData.getSkillBarData().set(slot, skill);
        player.syncData(ModAttachments.PLAYER_DATA);
        AscendantMod.LOGGER.info("Assigned {} to slot {}", skill.name(), slot);
    }

    public static void use(Player player, Skill skill) {
        if (player.level().isClientSide) {
            return;
        }
    
        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        if (!skill.isKnownBy(playerData) || skill != Skill.IDENTIFY) {
            return;
        }
    
        double reach = 5.0;
        var eye = player.getEyePosition();
        var end = eye.add(player.getViewVector(1.0F).scale(reach));
        var box = player.getBoundingBox().expandTowards(end.subtract(eye)).inflate(1.0);
    
        var entityHit = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
            player,
            eye,
            end,
            box,
            target -> target.isPickable() && !target.isSpectator(),
            reach * reach
        );
    
        var blockHit = player.pick(reach, 1.0F, false);
    
        boolean entityCloser = entityHit != null && (
            blockHit.getType() == net.minecraft.world.phys.HitResult.Type.MISS
            || eye.distanceToSqr(entityHit.getLocation()) <= eye.distanceToSqr(blockHit.getLocation())
        );
    
        if (entityCloser) {
            player.sendSystemMessage(Component.translatable(
                "message.ascendant.identify.entity",
                entityHit.getEntity().getName()
            ));
            return;
        }
    
        if (blockHit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK
            && blockHit instanceof net.minecraft.world.phys.BlockHitResult blockResult) {
            var state = player.level().getBlockState(blockResult.getBlockPos());
            player.sendSystemMessage(Component.translatable(
                "message.ascendant.identify.block",
                state.getBlock().getName()
            ));
            return;
        }
    
        player.sendSystemMessage(Component.translatable("message.ascendant.identify.nothing"));
    }
}