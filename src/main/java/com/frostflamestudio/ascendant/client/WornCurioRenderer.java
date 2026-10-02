package com.frostflamestudio.ascendant.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class WornCurioRenderer implements ICurioRenderer {
    public enum Placement {
        BACK,
        SIDE,
        CHEST
    }

    private final Placement placement;

    public WornCurioRenderer(Placement placement) {
        this.placement = placement;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
        ItemStack stack,
        SlotContext slotContext,
        PoseStack poseStack,
        RenderLayerParent<T, M> renderLayerParent,
        MultiBufferSource buffer,
        int light,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        LivingEntity entity = slotContext.entity();
        poseStack.pushPose();
        ICurioRenderer.translateIfSneaking(poseStack, entity);
        ICurioRenderer.rotateIfSneaking(poseStack, entity);

        switch (this.placement) {
            case BACK -> poseStack.translate(0.0, 0.55, 0.20);
            case SIDE -> {
                poseStack.translate(0.20, 0.65, 0.16);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-18));
            }
            case CHEST -> poseStack.translate(0.0, 0.60, -0.20);
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        if (this.placement != Placement.CHEST) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        }
        float scale = this.placement == Placement.CHEST ? 0.22F : 0.38F;
        poseStack.scale(scale, scale, scale);

        Minecraft.getInstance().getItemRenderer().renderStatic(
            stack,
            ItemDisplayContext.FIXED,
            light,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            buffer,
            entity.level(),
            entity.getId()
        );
        poseStack.popPose();
    }
}
