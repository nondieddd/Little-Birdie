package org.nondied.banda_de_trapo.client.camera;

import net.minecraft.entity.Entity;

public class PeriquitoCameraState {

    public enum Mode {
        IDLE,
        SHOW_SEQUENCE,
        PLAYER_TURN
    }

    public static boolean ACTIVE = false;
    public static Mode MODE = Mode.IDLE;

    public static Entity PERIQUITO = null;
    public static Entity PLAYER = null;

    // control orbital
    public static float yaw = 0;
    public static float pitch = 20f;
    public static float distance = 6f;

    public static void start(Entity periquito, Entity player) {
        ACTIVE = true;
        PERIQUITO = periquito;
        PLAYER = player;
        MODE = Mode.SHOW_SEQUENCE;

        yaw = 0;
        pitch = 20f;
        distance = 6f;
    }

    public static void stop() {
        ACTIVE = false;
        MODE = Mode.IDLE;
        PERIQUITO = null;
        PLAYER = null;
    }

    public static Entity getTarget() {
        if (MODE == Mode.SHOW_SEQUENCE) return PERIQUITO;
        if (MODE == Mode.PLAYER_TURN) return PLAYER;
        return null;
    }
}