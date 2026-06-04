package org.nondied.banda_de_trapo.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.nondied.banda_de_trapo.client.camera.OrbitalCameraSystem;

public class PlayerArrowRenderer {

    private static final Identifier TEXTURE = Identifier.of("banda_de_trapo", "textures/gui/arrows.png");
    private static final int SPRITE_SIZE = 16;
    private static final float ABOVE_HEAD = 0.9f;

    public static void register() {
        HudRenderCallback.EVENT.register(PlayerArrowRenderer::render);
    }

    private static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter ticker) {
        if (!OrbitalCameraSystem.isActive()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.gameRenderer == null) return;

        float delta = ticker.getTickDelta(true);

        double px = MathHelper.lerp(delta, client.player.prevX, client.player.getX());
        double py = MathHelper.lerp(delta, client.player.prevY, client.player.getY());
        double pz = MathHelper.lerp(delta, client.player.prevZ, client.player.getZ());

        double headY = py + client.player.getHeight() + ABOVE_HEAD;

        Camera camera = client.gameRenderer.getCamera();
        Vec3d cam = camera.getPos();

        float rx = (float)(px - cam.x);
        float ry = (float)(headY - cam.y);
        float rz = (float)(pz - cam.z);

        float yaw   = (float) Math.toRadians(camera.getYaw());
        float pitch = (float) Math.toRadians(camera.getPitch());

        Matrix4f view = new Matrix4f()
                .rotateX(pitch)
                .rotateY((float)(Math.PI) + yaw);

        Vector4f clip = new Vector4f(rx, ry, rz, 1f);
        view.transform(clip);

        if (clip.z >= 0f) return;

        double fovRad = Math.toRadians(client.options.getFov().getValue());
        float scW = client.getWindow().getScaledWidth();
        float scH = client.getWindow().getScaledHeight();
        float aspect = scW / scH;

        float projX = clip.x / (-clip.z) / (float)(Math.tan(fovRad / 2.0) * aspect);
        float projY = clip.y / (-clip.z) / (float)(Math.tan(fovRad / 2.0));

        int screenX = (int)((projX * 0.5f + 0.5f) * scW) - SPRITE_SIZE / 2;
        int screenY = (int)((1f - (projY * 0.5f + 0.5f)) * scH) - SPRITE_SIZE / 2;

        context.drawTexture(TEXTURE, screenX, screenY, 0, 0, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
    }
}