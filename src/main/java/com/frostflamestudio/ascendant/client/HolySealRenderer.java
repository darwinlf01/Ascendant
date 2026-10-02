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

public class HolySealRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        AscendantMod.MODID,
        "textures/entity/holy_seal.png"
    );
    private static final ModelPart BODY;

    static {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.5F, 0.2F, 1.6F, 5.0F, 1.0F, 1.0F)
                .texOffs(0, 4)
                .addBox(-2.8F, 0.2F, -2.2F, 1.0F, 1.0F, 4.0F)
                .texOffs(0, 4)
                .addBox(1.8F, 0.2F, -2.2F, 1.0F, 1.0F, 4.0F)
                .texOffs(0, 12)
                .addBox(-1.5F, 2.8F, -3.6F, 3.0F, 3.0F, 1.0F)
                .texOffs(12, 6)
                .addBox(-1.0F, 3.2F, -4.1F, 2.0F, 2.0F, 1.0F),
            PartPose.ZERO
        );
        body.addOrReplaceChild(
            "left_cord",
            CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(-2.0F, 0.8F, -2.0F, 0.55F, 0.0F, -0.4F)
        );
        body.addOrReplaceChild(
            "right_cord",
            CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(2.0F, 0.8F, -2.0F, 0.55F, 0.0F, 0.4F)
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
