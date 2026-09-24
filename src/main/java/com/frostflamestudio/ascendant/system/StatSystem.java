package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.StatType;
import com.frostflamestudio.ascendant.data.StatData;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class StatSystem {
    private static final ResourceLocation VITALITY_HEALTH_ID =
        ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "vitality_health");
    
    private static final ResourceLocation AGILITY_SPEED_ID =
        ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "agility_speed");

    private static final ResourceLocation STRENGTH_DAMAGE_ID =
        ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "strength_damage");

    private static final int SPRINT_DRAIN_INTERVAL = 40;

    private static final int STAMINA_REGEN_INTERVAL = 60;

    public static void applyVitalityHealth(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        var instance = player.getAttribute(Attributes.MAX_HEALTH);

        if (instance == null) {
            return;
        }

        int vitality = player.getData(ModAttachments.PLAYER_DATA)
                                .getStatData()
                                .get(StatType.VITALITY);

        double target = Math.max(1.0, vitality);
        double amount = target - instance.getBaseValue();

        instance.addOrUpdateTransientModifier(new AttributeModifier(
            VITALITY_HEALTH_ID, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void applyAgilitySpeed(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        var instance = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (instance == null) {
            return;
        }

        int agility = player.getData(ModAttachments.PLAYER_DATA)
                                .getStatData()
                                .get(StatType.AGILITY);

        double amount = (agility / (double) StatData.HUMAN_BASE) - 1.0;

        instance.addOrUpdateTransientModifier(new AttributeModifier(
            AGILITY_SPEED_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public static void applyStrengthDamage(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        var instance = player.getAttribute(Attributes.ATTACK_DAMAGE);

        if (instance == null) {
            return;
        }

        int strength = player.getData(ModAttachments.PLAYER_DATA)
                                .getStatData()
                                .get(StatType.STRENGTH);

        double amount = (strength / (double) StatData.HUMAN_BASE) - 1.0;

        instance.addOrUpdateTransientModifier(new AttributeModifier(
            STRENGTH_DAMAGE_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public static void applyToughnessReduction(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }

        int toughness = player.getData(ModAttachments.PLAYER_DATA)
                        .getStatData()
                        .get(StatType.TOUGHNESS);
        
        int safe = Math.max(1, toughness);
        float factor = StatData.HUMAN_BASE / (float) safe;

        event.setAmount(event.getAmount() * factor);
    }

    public static void tickStamina(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        var stats = player.getData(ModAttachments.PLAYER_DATA).getStatData();

        if (player.isSprinting()) {
            if (stats.getStamina() <= 0) {
                player.setSprinting(false);
                return;
            }
        
            if (player.tickCount % SPRINT_DRAIN_INTERVAL != 0) {
                return;
            }
        
            stats.setStamina(stats.getStamina() - 1);
            player.syncData(ModAttachments.PLAYER_DATA);
        
            if (stats.getStamina() <= 0) {
                player.setSprinting(false);
            }
            return;
        }

        if (stats.getStamina() >= stats.getMaxStamina()) {
            return;
        }
        
        if (player.tickCount % STAMINA_REGEN_INTERVAL != 0) {
            return;
        }
        
        stats.setStamina(stats.getStamina() + 1);
        player.syncData(ModAttachments.PLAYER_DATA);
    }
}
