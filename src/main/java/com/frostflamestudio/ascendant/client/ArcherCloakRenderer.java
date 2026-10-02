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

public class ArcherCloakRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        AscendantMod.MODID,
        "textures/entity/archer_cloak.png"
    );
    private static final ModelPart BODY;
    private static final ModelPart HOOD;
    private static final ModelPart CENTER;
    private static final ModelPart LEFT_FOLD;
    private static final ModelPart RIGHT_FOLD;

    static {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-6.0F, -1.0F, 3.5F, 12.0F, 6.0F, 1.0F)
                .texOffs(44, 0)
                .addBox(-6.2F, 0.0F, -1.0F, 1.0F, 7.0F, 5.0F)
                .texOffs(44, 14)
                .addBox(5.2F, 0.4F, 2.2F, 1.0F, 5.0F, 2.0F)
                .texOffs(54, 14)
                .addBox(-5.2F, 0.8F, -2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(54, 18)
                .addBox(1.4F, 8.0F, -3.6F, 2.0F, 3.0F, 1.0F),
            PartPose.ZERO
        );
        body.addOrReplaceChild(
            "center",
            CubeListBuilder.create().texOffs(0, 10).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 1.0F),
            PartPose.offset(0.0F, 2.2F, 3.45F)
        );
        body.addOrReplaceChild(
            "left_fold",
            CubeListBuilder.create().texOffs(20, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 11.0F, 1.0F),
            PartPose.offset(-4.2F, 2.6F, 3.7F)
        );
        body.addOrReplaceChild(
            "right_fold",
            CubeListBuilder.create().texOffs(32, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 10.0F, 1.0F),
            PartPose.offset(4.2F, 2.8F, 3.7F)
        );
        root.addOrReplaceChild(
            "hood",
            CubeListBuilder.create()
                .texOffs(0, 24)
                .addBox(-5.0F, -8.6F, 3.7F, 10.0F, 12.0F, 1.0F)
                .texOffs(0, 40)
                .addBox(-5.0F, -9.2F, -3.5F, 10.0F, 1.0F, 10.0F)
                .texOffs(26, 24)
                .addBox(-5.8F, -8.0F, -2.0F, 1.0F, 9.0F, 7.0F)
                .texOffs(46, 24)
                .addBox(4.8F, -8.0F, -2.0F, 1.0F, 9.0F, 7.0F),
            PartPose.ZERO
        );

        ModelPart baked = LayerDefinition.create(mesh, 64, 64).bakeRoot();
        BODY = baked.getChild("body");
        HOOD = baked.getChild("hood");
        CENTER = BODY.getChild("center");
        LEFT_FOLD = BODY.getChild("left_fold");
        RIGHT_FOLD = BODY.getChild("right_fold");
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
        HOOD.copyFrom(humanoid.head);

        LivingEntity entity = slotContext.entity();
        float lift = 0.04F;
        if (entity.isSprinting()) {
            lift += 0.16F;
        }
        if (entity.isCrouching()) {
            lift += 0.2F;
        }
        CENTER.xRot = lift;
        LEFT_FOLD.xRot = lift;
        RIGHT_FOLD.xRot = lift;
        LEFT_FOLD.zRot = 0.03F;
        RIGHT_FOLD.zRot = -0.03F;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        BODY.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        HOOD.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
}
