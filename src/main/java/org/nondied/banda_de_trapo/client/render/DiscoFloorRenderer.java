package org.nondied.banda_de_trapo.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.nondied.banda_de_trapo.block.DiscoFloorBlockEntity;
import org.joml.Matrix4f;
import java.awt.Color;

public class DiscoFloorRenderer implements BlockEntityRenderer<DiscoFloorBlockEntity> {

    public DiscoFloorRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public boolean rendersOutsideBoundingBox(DiscoFloorBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(DiscoFloorBlockEntity entity, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {

        int mode = entity.getMode();
        BlockPos pos = entity.getPos();
        BlockPos center = entity.getCenterPos();

        long worldTime = entity.getWorld() != null ? entity.getWorld().getTime() : 0;
        float t = (worldTime + tickDelta) * 0.05f;

        float dx = pos.getX() - center.getX();
        float dz = pos.getZ() - center.getZ();
        float dist = (float) Math.sqrt(dx * dx + dz * dz);
        float angle = (float) Math.atan2(dz, dx);

        int finalColor;
        float hue;

        switch (mode) {

            case 1: {
                float ring = (dist * 0.13f - t * 0.5f) % 1.0f;
                if (ring < 0) ring += 1.0f;

                hue = (dist * 0.04f - t * 0.08f) % 1.0f;
                if (hue < 0) hue += 1.0f;

                if (ring < 0.60f) {
                    float edgeFade = (float) Math.sin(ring / 0.60f * Math.PI);
                    float bright = 0.6f + 0.4f * edgeFade;
                    finalColor = Color.HSBtoRGB(hue, 1.0f, bright);
                } else {
                    finalColor = 0x000000;
                }
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 2: {
                double n = Math.sin(dx * 0.30 + t * 0.9) * Math.cos(dz * 0.28 + t * 0.7)
                        + Math.sin((dx + dz) * 0.18 - t * 0.5)
                        + Math.cos(dx * 0.12 - dz * 0.15 + t * 0.4);
                hue = (float) Math.max(0.0, Math.min(0.11, n * 0.04 + 0.04));
                float brightness2 = (float) Math.max(0.3, Math.min(1.0, n * 0.35 + 0.65));
                finalColor = Color.HSBtoRGB(hue, 1.0f, brightness2);
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 3: {
                float arms = 4.0f;
                float normalizedAngle = (float)(angle / (Math.PI * 2.0) + 0.5);
                float spiralParam = (normalizedAngle * arms + dist * 0.10f - t * 0.25f) % 1.0f;
                if (spiralParam < 0) spiralParam += 1.0f;

                hue = (normalizedAngle + t * 0.04f) % 1.0f;
                if (hue < 0) hue += 1.0f;

                float armWidth = Math.max(0.3f, 1.8f - dist * 0.035f);
                float bright3 = (float) Math.pow(Math.sin(spiralParam * Math.PI), armWidth) * 0.95f;

                finalColor = Color.HSBtoRGB(hue, 0.95f, Math.max(0f, bright3));
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 4: {
                float stripePos = (dx + dz) * 0.55f - t * 3.5f;
                float stripe = stripePos % 14.0f;
                if (stripe < 0) stripe += 14.0f;

                if (stripe >= 0.5f && stripe <= 8.5f) {
                    float bandPos = (stripe - 0.5f) / 8.0f;
                    float fadeSin = (float) Math.sin(bandPos * Math.PI);
                    hue = ((dx + dz) * 0.055f - t * 0.10f) % 1.0f;
                    if (hue < 0) hue += 1.0f;
                    float brightness = 0.3f + 0.7f * fadeSin;
                    finalColor = Color.HSBtoRGB(hue, 1.0f, brightness);
                } else {
                    finalColor = 0x000000;
                }
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 5: {
                float wave = (float)(Math.sin(dx * 0.22 + t * 0.5) + Math.cos(dz * 0.22 + t * 0.4));
                hue = (wave * 0.18f + (dx + dz) * 0.04f - t * 0.12f) % 1.0f;
                if (hue < 0) hue += 1.0f;
                finalColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 6: {
                float wave6a = (float) Math.sin(dx * 0.20 + t * 0.45);
                float wave6b = (float) Math.cos(dz * 0.18 + t * 0.38);
                float waveSum = wave6a + wave6b;

                hue = (waveSum * 0.18f + (dx + dz) * 0.04f - t * 0.12f) % 1.0f;
                if (hue < 0) hue += 1.0f;

                float brightWave = (float)(
                        Math.sin(dx * 0.15 + dz * 0.10 + t * 0.30) * 0.4
                                + Math.cos(dx * 0.08 - dz * 0.14 + t * 0.25) * 0.3
                                + 0.5
                );
                float brightness6 = Math.max(0.0f, Math.min(1.0f, brightWave));
                brightness6 = brightness6 * brightness6;

                finalColor = Color.HSBtoRGB(hue, 1.0f, brightness6);
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 7: {
                float rays = 12.0f;
                float ray = (float)(Math.sin(angle * rays + t * 0.5) * 0.5 + 0.5);
                float fade = 1.0f / (1.0f + dist * 0.08f);
                float intensity = (float) Math.pow(ray, 3.0) * fade;

                float r7, g7, b7;
                if (intensity < 0.35f) {
                    float x = intensity / 0.35f;
                    r7 = x; g7 = x * 0.35f; b7 = 0;
                } else if (intensity < 0.7f) {
                    float x = (intensity - 0.35f) / 0.35f;
                    r7 = 1.0f; g7 = 0.35f + x * 0.5f; b7 = x * 0.1f;
                } else {
                    float x = (intensity - 0.7f) / 0.3f;
                    r7 = 1.0f; g7 = 0.85f + x * 0.13f; b7 = 0.1f + x * 0.75f;
                }
                finalColor = new Color(Math.min(1,r7), Math.min(1,g7), Math.min(1,b7)).getRGB();
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 8: {
                float wave1 = (float)(Math.sin(dist * 0.4 - t * 2.0) * 0.5 + 0.5);
                float wave2 = (float)(Math.sin(dist * 0.4 - t * 2.0 + 2.1) * 0.5 + 0.5);
                float wave3 = (float)(Math.sin(dist * 0.4 - t * 2.0 + 4.2) * 0.5 + 0.5);
                float r8 = (float) Math.pow(wave1, 2.5);
                float g8 = (float) Math.pow(wave2, 4.0) * 0.4f;
                float b8 = (float) Math.pow(wave3, 2.5) * 0.6f;
                float fade8 = Math.max(0, Math.min(1, 1.0f - dist * 0.05f));
                finalColor = new Color(Math.min(1,r8*fade8), Math.min(1,g8*fade8), Math.min(1,b8*fade8)).getRGB();
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 9: {
                float cx = (float) Math.floor(pos.getX());
                float cz = (float) Math.floor(pos.getZ());
                float hash  = (float)(Math.abs(Math.sin(cx * 127.1 + cz * 311.7) * 43758.5453) % 1.0);
                float hash2 = (float)(Math.abs(Math.sin(cx * 269.5 + cz * 183.3) * 12345.6789) % 1.0);
                float hash3 = (float)(Math.abs(Math.sin(cx * 419.2 + cz * 371.9) * 99999.1234) % 1.0);

                float twinkle = (float)(Math.sin(t * (0.3 + hash * 0.8) + hash2 * 6.28) * 0.5 + 0.5);
                twinkle = (float) Math.pow(twinkle, 3.0);
                float isStar = hash > 0.70f ? 1.0f : 0.0f;
                float brightness9 = twinkle * isStar;

                float r9, g9, b9;
                if (hash > 0.95f) { r9=1.0f; g9=0.4f; b9=0.2f; }
                else if (hash > 0.85f) { r9=1.0f; g9=0.9f; b9=0.4f; }
                else { r9=0.7f; g9=0.85f; b9=1.0f; }

                float bgb = 0.01f + 0.03f * hash3;
                finalColor = new Color(
                        Math.min(1, 0.01f + r9 * brightness9),
                        Math.min(1, 0.01f + g9 * brightness9),
                        Math.min(1, bgb  + b9 * brightness9)
                ).getRGB();
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 10: {
                double n10 = Math.sin(dx * 0.26 + t * 0.78) * Math.cos(dz * 0.24 + t * 0.62)
                        + Math.sin((dx + dz) * 0.16 - t * 0.48)
                        + Math.cos(dx * 0.09 - dz * 0.12 + t * 0.35);
                hue = (float) Math.max(0.72, Math.min(0.92, n10 * 0.06 + 0.82));
                float brightness10 = (float) Math.max(0.20, Math.min(1.0, n10 * 0.37 + 0.58));
                float sat10 = (float) Math.max(0.75, Math.min(1.0, 0.88 + n10 * 0.06));
                finalColor = Color.HSBtoRGB(hue, sat10, brightness10);
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            case 0: {
                finalColor = new Color(0.06f, 0.06f, 0.08f).getRGB();
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }

            default: {
                hue = ((dx + dz) * 0.08f - t * 0.4f) % 1.0f;
                if (hue < 0) hue += 1.0f;
                finalColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                drawQuad(matrices, vertexConsumers, entity, finalColor);
                return;
            }
        }
    }

    private void drawQuad(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                          DiscoFloorBlockEntity entity, int rgb) {

        float r = ((rgb >> 16) & 0xFF) / 255.0f;
        float g = ((rgb >>  8) & 0xFF) / 255.0f;
        float b = ( rgb        & 0xFF) / 255.0f;

        Sprite sprite = MinecraftClient.getInstance()
                .getBlockRenderManager()
                .getModels()
                .getModel(entity.getCachedState())
                .getParticleSprite();

        float u0 = sprite.getMinU(), u1 = sprite.getMaxU();
        float v0 = sprite.getMinV(), v1 = sprite.getMaxV();

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getSolid());
        Matrix4f mat = matrices.peek().getPositionMatrix();
        float h = 1.001f;

        consumer.vertex(mat, 0, h, 0).color(r,g,b,1f).texture(u0,v0).light(15728880).normal(0,1,0);
        consumer.vertex(mat, 0, h, 1).color(r,g,b,1f).texture(u0,v1).light(15728880).normal(0,1,0);
        consumer.vertex(mat, 1, h, 1).color(r,g,b,1f).texture(u1,v1).light(15728880).normal(0,1,0);
        consumer.vertex(mat, 1, h, 0).color(r,g,b,1f).texture(u1,v0).light(15728880).normal(0,1,0);
    }
}