package com.peeko32213.unusualprehistory.client.render.dinosaur_renders;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.peeko32213.unusualprehistory.client.model.MammothModel;
import com.peeko32213.unusualprehistory.client.model.MegatheriumModel;
import com.peeko32213.unusualprehistory.common.entity.EntityMammoth;
import com.peeko32213.unusualprehistory.common.entity.EntityMegatherium;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MammothRenderer extends GeoEntityRenderer<EntityMammoth> {

    public MammothRenderer(EntityRendererProvider.Context context) {
        super(context, new MammothModel());
    }

    @Override
    public void renderEarly(EntityMammoth animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (animatable.isBaby()) {
            stackIn.scale(0.5F, 0.5F, 0.5F);
        }
    }

}
