package org.nondied.banda_de_trapo.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PeriquitoRenderer extends GeoEntityRenderer<PeriquitoEntity> {

    public PeriquitoRenderer(EntityRendererFactory.Context context) {
        super(context, new PeriquitoModel());
    }

    @Override
    public void render(PeriquitoEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}