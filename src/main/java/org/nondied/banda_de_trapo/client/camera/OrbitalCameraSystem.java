package org.nondied.banda_de_trapo.client.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;
import org.nondied.banda_de_trapo.mixin.CameraAccessor;

public class OrbitalCameraSystem {

    public enum Mode {
        INACTIVE,
        PERIQUITO,
        PLAYER
    }

    public static Mode mode = Mode.INACTIVE;
    private static Entity target;
    private static Vec3d currentPos = null;
    private static float currentYaw = 0f;
    private static float currentPitch = 18f;
    private static float orbitYaw = 0f;
    private static float targetOrbitYaw = 0f;
    private static final float PLAYER_DISTANCE = 3.5f;
    private static final float PLAYER_HEIGHT = 1.2f;
    private static final double PLAYER_VERTICAL_OFFSET = 1.3f;
    private static final float PERIQUITO_DISTANCE = 7.1f;
    private static final float PERIQUITO_HEIGHT = 4.2f;
    private static final long TRANSITION_DURATION_NS = 1_400_000_000L;
    private static boolean inTransition = false;
    private static long transitionStartNs = 0L;
    private static Vec3d fromPos = null;
    private static float fromYaw = 0f;
    private static float fromPitch = 0f;
    public static boolean isActive() {
        return mode != Mode.INACTIVE;
    }

    public static void activatePlayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (mode == Mode.PLAYER && target == client.player) return;

        target = client.player;
        mode = Mode.PLAYER;
        inTransition = false;
        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    public static void activatePeriquito(PeriquitoEntity periquito) {
        if (periquito == null) return;
        if (mode == Mode.PERIQUITO && target == periquito) return;

        MinecraftClient client = MinecraftClient.getInstance();
        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        if (currentPos == null && client.player != null) {
            Entity player = client.player;
            Vec3d playerFocus = new Vec3d(
                    player.getX(),
                    player.getY() + PLAYER_HEIGHT,
                    player.getZ()
            );
            float yawRad = (float) Math.toRadians(player.getYaw());
            double ox = Math.sin(yawRad) * PLAYER_DISTANCE;
            double oz = -Math.cos(yawRad) * PLAYER_DISTANCE;

            currentPos   = playerFocus.add(ox, PLAYER_VERTICAL_OFFSET, oz);
            currentYaw   = lookYaw(currentPos, playerFocus);
            currentPitch = lookPitch(currentPos, playerFocus);
            orbitYaw       = player.getYaw();
            targetOrbitYaw = orbitYaw;
        }

        target = periquito;
        mode   = Mode.PERIQUITO;
        inTransition      = true;
        transitionStartNs = System.nanoTime();
        fromPos           = currentPos;
        fromYaw           = currentYaw;
        fromPitch         = currentPitch;
    }

    public static void deactivate() {
        mode         = Mode.INACTIVE;
        target       = null;
        currentPos   = null;
        inTransition = false;
    }
    public static void applyMouseDragX(double deltaX) {
        if (mode != Mode.PLAYER) return;
        targetOrbitYaw += (float) deltaX * 0.35f;
    }
    public static void update(Camera camera, float tickDelta) {
        if (mode == Mode.INACTIVE || target == null) return;
        Vec3d desiredPos;
        float desiredYaw;
        float desiredPitch;

        if (mode == Mode.PERIQUITO) {
            Vec3d base       = lerpEntityPos(target, tickDelta);
            Vec3d focusPoint = base.add(0.0, PERIQUITO_HEIGHT, 0.0);

            Vec3d forward = target.getRotationVec(tickDelta).normalize();
            if (forward.lengthSquared() < 0.0001) forward = new Vec3d(0, 0, 1);

            desiredPos   = focusPoint.add(forward.multiply(PERIQUITO_DISTANCE));
            desiredYaw   = lookYaw(desiredPos, focusPoint);
            desiredPitch = lookPitch(desiredPos, focusPoint);

        } else {
            Vec3d base       = lerpEntityPos(target, tickDelta);
            Vec3d focusPoint = base.add(0.0, PLAYER_HEIGHT, 0.0);

            orbitYaw = MathHelper.lerpAngleDegrees(0.18f, orbitYaw, targetOrbitYaw);

            double yawRad  = Math.toRadians(orbitYaw);
            double offsetX = -Math.sin(yawRad) * PLAYER_DISTANCE;
            double offsetZ =  Math.cos(yawRad) * PLAYER_DISTANCE;

            desiredPos   = focusPoint.add(offsetX, PLAYER_VERTICAL_OFFSET, offsetZ);
            desiredYaw   = lookYaw(desiredPos, focusPoint);
            desiredPitch = lookPitch(desiredPos, focusPoint);
        }
        if (currentPos == null) {
            currentPos   = desiredPos;
            currentYaw   = desiredYaw;
            currentPitch = desiredPitch;

        } else if (inTransition) {
            float t     = Math.min(1.0f, (float)(System.nanoTime() - transitionStartNs) / TRANSITION_DURATION_NS);
            float eased = easeInOutCubic(t);

            currentPos   = fromPos.lerp(desiredPos, eased);
            currentYaw   = lerpAngle(fromYaw, desiredYaw, eased);
            currentPitch = MathHelper.lerp(eased, fromPitch, desiredPitch);

            if (t >= 1.0f) inTransition = false;

        } else {
            currentPos   = currentPos.lerp(desiredPos, 0.18);
            currentYaw   = MathHelper.lerpAngleDegrees(0.18f, currentYaw, desiredYaw);
            currentPitch = MathHelper.lerp(0.18f, currentPitch, desiredPitch);
        }
        CameraAccessor accessor = (CameraAccessor) camera;
        accessor.invokeSetPos(currentPos);
        accessor.invokeSetRotation(currentYaw, currentPitch);
    }
    private static float easeInOutCubic(float t) {
        return t < 0.5f
                ? 4f * t * t * t
                : 1f - (float) Math.pow(-2f * t + 2f, 3) / 2f;
    }
    private static float lerpAngle(float from, float to, float t) {
        float delta = to - from;
        while (delta >  180f) delta -= 360f;
        while (delta < -180f) delta += 360f;
        return from + delta * t;
    }

    private static Vec3d lerpEntityPos(Entity entity, float tickDelta) {
        return new Vec3d(
                MathHelper.lerp(tickDelta, entity.prevX, entity.getX()),
                MathHelper.lerp(tickDelta, entity.prevY, entity.getY()),
                MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ())
        );
    }

    private static float lookYaw(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        return (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
    }

    private static float lookPitch(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double h  = Math.sqrt(dx * dx + dz * dz);
        return (float)(-Math.toDegrees(Math.atan2(dy, h)));
    }
}