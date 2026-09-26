package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.Skill;
import com.frostflamestudio.ascendant.data.SkillBarData;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.minecraft.world.entity.player.Player;

public class SkillSystem {
    public static void assignSlot(Player player, Skill skill, int slot) {
        if (slot < 0 || slot >= SkillBarData.SLOT_COUNT) {
            return;
        }

        if (skill.getKind() != Skill.Kind.ACTIVE) {
            return;
        }

        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        if (skill.getPlayerClass() != playerData.getPlayerClass()) {
            return;
        }

        playerData.getSkillBarData().set(slot, skill);
        player.syncData(ModAttachments.PLAYER_DATA);
        AscendantMod.LOGGER.info("Assigned {} to slot {}", skill.name(), slot);
    }
}