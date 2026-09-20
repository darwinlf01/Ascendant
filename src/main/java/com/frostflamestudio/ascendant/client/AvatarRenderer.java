package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.entity.AvatarEntity;
import com.frostflamestudio.ascendant.registry.ModEntities;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = AscendantMod.MODID, value = Dist.CLIENT)
public class AvatarRenderer extends HumanoidMobRenderer<AvatarEntity, PlayerModel<AvatarEntity>> {
    public AvatarRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<AvatarEntity>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AvatarEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "textures/entity/avatar.png");
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.AVATAR.get(), AvatarRenderer::new);
    }
}
