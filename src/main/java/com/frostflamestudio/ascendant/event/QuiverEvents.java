package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.system.QuiverSystem;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class QuiverEvents {
    @SubscribeEvent
    public static void onArrowJoin(EntityJoinLevelEvent event) {
        QuiverSystem.onArrowJoin(event);
    }

    @SubscribeEvent
    public static void onGetProjectile(LivingGetProjectileEvent event) {
        QuiverSystem.supplyArrow(event);
    }
}