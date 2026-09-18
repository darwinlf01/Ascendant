package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.entity.AvatarEntity;
import com.frostflamestudio.ascendant.registry.ModEntities;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.AVATAR.get(), AvatarEntity.createAttributes().build());
    }
}
