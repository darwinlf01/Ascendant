package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.entity.AvatarEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(Registries.ENTITY_TYPE, AscendantMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<AvatarEntity>> AVATAR =
        ENTITY_TYPES.register(
            "avatar",
            () -> EntityType.Builder.<AvatarEntity>of(AvatarEntity::new, MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .clientTrackingRange(8)
                .build("avatar")
        );
}
