package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.AscendantMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class LeatherQuiverRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        AscendantMod.MODID,
        "textures/entity/leather_quiver.png"
    );
    private static final ModelPart BODY;

    static {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.2F, 1.8F, 5.1F, 2.0F, 8.0F, 2.0F)
                .texOffs(10, 0)
                .addBox(-4.6F, 1.2F, 4.7F, 3.0F, 1.0F, 3.0F)
                .texOffs(8, 12)
                .addBox(-3.9F, -1.4F, 5.6F, 1.0F, 4.0F, 1.0F)
                .texOffs(8, 12)
                .addBox(-3.1F, -0.8F, 6.2F, 1.0F, 3.0F, 1.0F)
                .texOffs(14, 12)
                .addBox(-4.1F, -2.2F, 5.4F, 1.0F, 2.0F, 1.0F)
                .texOffs(14, 12)
                .addBox(-3.3F, -1.6F, 6.0F, 1.0F, 2.0F, 1.0F),
            PartPose.ZERO
        );
        body.addOrReplaceChild(
            "strap",
            CubeListBuilder.create().texOffs(0, 12).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
            PartPose.offsetAndRotation(-3.2F, 0.2F, 4.6F, 0.45F, 0.15F, 0.7F)
        );
        BODY = LayerDefinition.create(mesh, 64, 64).bakeRoot().getChild("body");
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
        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoid)) {
            return;
        }

        BODY.copyFrom(humanoid.body);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        BODY.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
}
