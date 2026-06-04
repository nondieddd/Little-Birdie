package org.nondied.banda_de_trapo.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.nondied.banda_de_trapo.client.camera.OrbitalCameraSystem;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;
import org.nondied.banda_de_trapo.network.ModNetworking;
import org.nondied.banda_de_trapo.sound.ModSounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BandaDeTrapoScreen extends Screen {

    private Perspective previousPerspective;
    private final int totalRounds;
    private final int squaresPerRound;
    private boolean waitingForServer = false;
    private int playersPassed = 0;
    private int totalPlayers = 1;

    private static final Identifier PLAYER_ICON = Identifier.of("banda_de_trapo", "textures/gui/miniplayer.png");
    private static final Identifier HEART_SPRITE = Identifier.of("banda_de_trapo", "textures/gui/corazon_completo.png");
    private static final Identifier CONTAINER_SPRITE = Identifier.of("banda_de_trapo", "textures/gui/corazon_incompleto.png");
    private static final Identifier CURSOR_TEX = Identifier.of("banda_de_trapo", "textures/gui/cursor.png");
    private static final Identifier RED_TEX = Identifier.of("banda_de_trapo", "textures/gui/red.png");
    private static final Identifier GREEN_TEX = Identifier.of("banda_de_trapo", "textures/gui/green.png");

    private static final Identifier TEX_0_CIAN_CLICK = Identifier.of("banda_de_trapo", "textures/gui/5_3.png");
    private static final Identifier TEX_0_CIAN_HOVER = Identifier.of("banda_de_trapo", "textures/gui/5_2.png");
    private static final Identifier TEX_0_CIAN = Identifier.of("banda_de_trapo", "textures/gui/5.png");
    private static final Identifier TEX_1_MARRON_CLICK = Identifier.of("banda_de_trapo", "textures/gui/6_3.png");
    private static final Identifier TEX_1_MARRON_HOVER = Identifier.of("banda_de_trapo", "textures/gui/6_2.png");
    private static final Identifier TEX_1_MARRON = Identifier.of("banda_de_trapo", "textures/gui/6.png");
    private static final Identifier TEX_2_GRIS_CLICK = Identifier.of("banda_de_trapo", "textures/gui/7_3.png");
    private static final Identifier TEX_2_GRIS_HOVER = Identifier.of("banda_de_trapo", "textures/gui/7_2.png");
    private static final Identifier TEX_2_GRIS = Identifier.of("banda_de_trapo", "textures/gui/7.png");
    private static final Identifier TEX_3_MORADO_CLICK = Identifier.of("banda_de_trapo", "textures/gui/8_3.png");
    private static final Identifier TEX_3_MORADO_HOVER = Identifier.of("banda_de_trapo", "textures/gui/8_2.png");
    private static final Identifier TEX_3_MORADO = Identifier.of("banda_de_trapo", "textures/gui/8.png");
    private static final Identifier TEX_4_VERDE_CLICK = Identifier.of("banda_de_trapo", "textures/gui/9_3.png");
    private static final Identifier TEX_4_VERDE_HOVER = Identifier.of("banda_de_trapo", "textures/gui/9_2.png");
    private static final Identifier TEX_4_VERDE = Identifier.of("banda_de_trapo", "textures/gui/9.png");
    private static final Identifier TEX_5_AMARILLO_CLICK = Identifier.of("banda_de_trapo", "textures/gui/10_3.png");
    private static final Identifier TEX_5_AMARILLO_HOVER = Identifier.of("banda_de_trapo", "textures/gui/10_2.png");
    private static final Identifier TEX_5_AMARILLO = Identifier.of("banda_de_trapo", "textures/gui/10.png");
    private static final Identifier TEX_6_CELESTE_CLICK = Identifier.of("banda_de_trapo", "textures/gui/11_3.png");
    private static final Identifier TEX_6_CELESTE_HOVER = Identifier.of("banda_de_trapo", "textures/gui/11_2.png");
    private static final Identifier TEX_6_CELESTE = Identifier.of("banda_de_trapo", "textures/gui/11.png");
    private static final Identifier TEX_7_ROSA_CLICK = Identifier.of("banda_de_trapo", "textures/gui/1_3.png");
    private static final Identifier TEX_7_ROSA_HOVER = Identifier.of("banda_de_trapo", "textures/gui/1_2.png");
    private static final Identifier TEX_7_ROSA = Identifier.of("banda_de_trapo", "textures/gui/1.png");
    private static final Identifier TEX_8_AZUL_CLICK = Identifier.of("banda_de_trapo", "textures/gui/2_3.png");
    private static final Identifier TEX_8_AZUL_HOVER = Identifier.of("banda_de_trapo", "textures/gui/2_2.png");
    private static final Identifier TEX_8_AZUL = Identifier.of("banda_de_trapo", "textures/gui/2.png");
    private static final Identifier TEX_9_NARANJA_CLICK = Identifier.of("banda_de_trapo", "textures/gui/3_3.png");
    private static final Identifier TEX_9_NARANJA_HOVER = Identifier.of("banda_de_trapo", "textures/gui/3_2.png");
    private static final Identifier TEX_9_NARANJA = Identifier.of("banda_de_trapo", "textures/gui/3.png");
    private static final Identifier TEX_10_ROJO_CLICK = Identifier.of("banda_de_trapo", "textures/gui/4_3.png");
    private static final Identifier TEX_10_ROJO_HOVER = Identifier.of("banda_de_trapo", "textures/gui/4_2.png");
    private static final Identifier TEX_10_ROJO = Identifier.of("banda_de_trapo", "textures/gui/4.png");

    private static final int CURSOR_SIZE = 16;
    private static final int MUSIC_DURATION_MS = 32000;
    private static final int CLICK_TIME_LIMIT_MS = 5000;

    private enum GameState {
        PRE_MUSIC_DELAY, PLAYING_MUSIC, INTRO, SHOWING_PATTERN, PLAYING,
        SUCCESS_SCREEN, FAILED_SCREEN, WAITING_FOR_SERVER, GAME_OVER
    }
    private GameState currentState = GameState.PRE_MUSIC_DELAY;

    private long stateStartTime;
    private long screenOpenTime;
    private long lastCorrectClickTime = 0;
    private int currentRound = 1;
    private int lives = 5;

    private final List<Integer> currentPattern = new ArrayList<>();
    private int lastSoundIndex = -1;
    private int playerClickProgress = 0;
    private int lastHoveredSquare = -1;
    private int lastClickedSquareId = -1;

    private SoundInstance idleSoundInstance = null;

    private record ClickableSquare(int id, int x, int y, int size) {}
    private final List<ClickableSquare> activeButtons = new ArrayList<>();

    private PeriquitoEntity targetPeriquito = null;
    private boolean alreadyInitialized = false;
    private OrbitalCameraSystem.Mode lastCameraMode = OrbitalCameraSystem.Mode.INACTIVE;

    private int cursorX = 0;
    private int cursorY = 0;

    public BandaDeTrapoScreen(int totalRounds, int squaresPerRound) {
        super(Text.literal("Banda de Trapo"));
        this.totalRounds = totalRounds;
        this.squaresPerRound = squaresPerRound;
    }

    @Override
    protected void init() {
        super.init();

        if (this.client != null) {
            this.previousPerspective = this.client.options.getPerspective();
            this.client.options.setPerspective(Perspective.FIRST_PERSON);
            this.client.options.hudHidden = true;
            GLFW.glfwSetInputMode(this.client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }

        if (!alreadyInitialized) {
            alreadyInitialized = true;
            this.screenOpenTime = System.currentTimeMillis();

            if (this.client != null && this.client.player != null && this.client.world != null) {
                this.targetPeriquito = this.client.world.getEntitiesByClass(
                        PeriquitoEntity.class,
                        this.client.player.getBoundingBox().expand(256.0),
                        entity -> true
                ).stream().findFirst().orElse(null);
            }
            startGame();
        }
    }

    private void startGame() {
        currentRound = 1;
        lives = 5;
        changeState(GameState.PRE_MUSIC_DELAY);
    }

    public void updateProgress(int passed, int total) {
        this.playersPassed = passed;
        this.totalPlayers = total;
    }

    public void startNextRound(int round) {
        this.currentRound = round;
        this.waitingForServer = false;
        changeState(GameState.PRE_MUSIC_DELAY);
    }

    public void forceEndGame(boolean won) {
        if (won) this.close();
        else changeState(GameState.GAME_OVER);
    }

    private void changeState(GameState newState) {
        this.currentState = newState;
        this.stateStartTime = System.currentTimeMillis();
        if (this.client == null) return;

        if (idleSoundInstance != null && newState != GameState.PLAYING) {
            this.client.getSoundManager().stop(idleSoundInstance);
            idleSoundInstance = null;
        }

        switch (newState) {
            case PLAYING_MUSIC -> this.client.getSoundManager().play(PositionedSoundInstance.master(ModSounds.PERIQUITO, 1.0f, 5.0f));
            case PLAYING -> {
                idleSoundInstance = PositionedSoundInstance.master(ModSounds.IDLE, 1.0f, 5.0f);
                this.client.getSoundManager().play(idleSoundInstance);
            }
            case SUCCESS_SCREEN -> this.client.getSoundManager().play(PositionedSoundInstance.master(ModSounds.PASS, 1.0f, 5.0f));
            case FAILED_SCREEN -> this.client.getSoundManager().play(PositionedSoundInstance.master(ModSounds.LOSE, 1.0f, 5.0f));
            default -> {}
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (waitingForServer || currentState == GameState.GAME_OVER) return;

        long current = System.currentTimeMillis();
        long elapsed = current - stateStartTime;

        if (currentState == GameState.PRE_MUSIC_DELAY && elapsed > 2000) {
            changeState(GameState.PLAYING_MUSIC);
        } else if (currentState == GameState.PLAYING_MUSIC && elapsed > MUSIC_DURATION_MS) {
            changeState(GameState.INTRO);
        } else if (currentState == GameState.INTRO && elapsed > 3000) {
            lastSoundIndex = -1;
            currentPattern.clear();
            List<Integer> availableColors = new ArrayList<>();
            for (int i = 0; i <= 10; i++) availableColors.add(i);
            Collections.shuffle(availableColors);
            for (int i = 0; i < squaresPerRound; i++) currentPattern.add(availableColors.get(i));
            changeState(GameState.SHOWING_PATTERN);
        } else if (currentState == GameState.SHOWING_PATTERN) {
            int newIndex = (int) (elapsed / 2000);
            if (newIndex < squaresPerRound && newIndex != lastSoundIndex) {
                lastSoundIndex = newIndex;
                int currentSquareId = currentPattern.get(newIndex);
                if (this.client != null)
                    this.client.getSoundManager().play(PositionedSoundInstance.master(getSquareSound(currentSquareId), 1.0f, 5.0f));
                if (this.targetPeriquito != null)
                    this.targetPeriquito.playEmote(currentSquareId);
            }
            if (elapsed >= (squaresPerRound * 2000L)) {
                playerClickProgress = 0;
                lastSoundIndex = -1;
                if (targetPeriquito != null) targetPeriquito.stopAnimation();
                changeState(GameState.PLAYING);
            }
        } else if (currentState == GameState.PLAYING) {
            long referenceTime = (playerClickProgress == 0) ? stateStartTime : lastCorrectClickTime;
            if (current - referenceTime >= CLICK_TIME_LIMIT_MS) {
                this.lives--;
                changeState(GameState.FAILED_SCREEN);
            }
        } else if (currentState == GameState.SUCCESS_SCREEN && elapsed > 2000) {
            waitingForServer = true;
            ClientPlayNetworking.send(new ModNetworking.RoundResultPayload(true, lives));
            changeState(GameState.WAITING_FOR_SERVER);
        } else if (currentState == GameState.FAILED_SCREEN && elapsed > 2000) {
            waitingForServer = true;
            ClientPlayNetworking.send(new ModNetworking.RoundResultPayload(false, lives));
            if (lives <= 0) changeState(GameState.GAME_OVER);
            else changeState(GameState.WAITING_FOR_SERVER);
        }

        updateCameraState();
    }

    private void updateCameraState() {
        if (this.client == null) return;
        boolean periquitoPhase = currentState == GameState.PRE_MUSIC_DELAY || currentState == GameState.PLAYING_MUSIC || currentState == GameState.INTRO || currentState == GameState.SHOWING_PATTERN;
        boolean playerPhase = currentState == GameState.PLAYING;
        OrbitalCameraSystem.Mode desiredMode = periquitoPhase && targetPeriquito != null ? OrbitalCameraSystem.Mode.PERIQUITO : playerPhase ? OrbitalCameraSystem.Mode.PLAYER : OrbitalCameraSystem.Mode.INACTIVE;

        if (desiredMode != lastCameraMode) {
            lastCameraMode = desiredMode;
            switch (desiredMode) {
                case PERIQUITO -> OrbitalCameraSystem.activatePeriquito(targetPeriquito);
                case PLAYER -> OrbitalCameraSystem.activatePlayer();
                case INACTIVE -> OrbitalCameraSystem.deactivate();
            }
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        cursorX = mouseX;
        cursorY = mouseY;
        if (this.client != null) {
            GLFW.glfwSetInputMode(this.client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }

        int screenWidth = this.width;
        int screenHeight = this.height;

        if (currentState == GameState.FAILED_SCREEN) {
            RenderSystem.enableBlend();
            context.drawTexture(RED_TEX, 0, 0, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
            RenderSystem.disableBlend();
        } else if (System.currentTimeMillis() - lastCorrectClickTime < 250 && currentState != GameState.PRE_MUSIC_DELAY && currentState != GameState.WAITING_FOR_SERVER) {
            RenderSystem.enableBlend();
            context.drawTexture(GREEN_TEX, 0, 0, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
            RenderSystem.disableBlend();
        }

        super.render(context, mouseX, mouseY, delta);

        if (currentState != GameState.GAME_OVER) {
            drawBaseUI(context, screenWidth, screenHeight, mouseX, mouseY);
            handleHoverSound(mouseX, mouseY);
            drawTopRightPlayerCounter(context, screenWidth);
        }

        if (currentState == GameState.INTRO) {
            drawScaledCenteredText(context, "¡Pon atención!", screenWidth / 2, 40, 0xFFFFAA00, 1.5f);
            drawScaledCenteredText(context, "¡Memoriza el patrón!", screenWidth / 2, 65, 0xFFFFFFFF, 1.2f);
        } else if (currentState == GameState.SHOWING_PATTERN) {
            drawPatternSequence(context, screenWidth, screenHeight);
        } else if (currentState == GameState.PLAYING || currentState == GameState.SUCCESS_SCREEN) {
            drawPlayingUI(context, screenWidth);
        } else if (currentState == GameState.FAILED_SCREEN) {
            drawBreakingHeartAnimation(context, screenWidth, screenHeight);
        } else if (currentState == GameState.WAITING_FOR_SERVER) {
            drawScaledCenteredText(context, "Esperando a los demás jugadores...", screenWidth / 2, screenHeight / 2, 0xFFAAAAAA, 1.2f);
        } else if (currentState == GameState.GAME_OVER) {
            context.fill(0, 0, screenWidth, screenHeight, 0xDD000000);
            drawScaledCenteredText(context, "¡HAS PERDIDO EL JUEGO!", screenWidth / 2, screenHeight / 2 - 20, 0xFFFF5555, 1.5f);
            drawScaledCenteredText(context, "Fuiste eliminado", screenWidth / 2, screenHeight / 2 + 10, 0xFFAAAAAA, 1.0f);
        }

        if (currentState == GameState.PLAYING && lastHoveredSquare != -1) {
            context.drawTooltip(this.textRenderer, Text.literal(getSquareName(lastHoveredSquare)), mouseX, mouseY);
        }

        drawCustomCursor(context);
    }

    private void drawCustomCursor(DrawContext context) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        context.drawTexture(CURSOR_TEX, cursorX, cursorY, 0, 0, CURSOR_SIZE, CURSOR_SIZE, CURSOR_SIZE, CURSOR_SIZE);
        RenderSystem.disableBlend();
    }

    private void drawPlayingUI(DrawContext context, int screenWidth) {
        int secondsLeft;
        int timerColor;

        if (currentState == GameState.SUCCESS_SCREEN) {
            timerColor = 0xFF55FF55;
            secondsLeft = (int) Math.ceil((CLICK_TIME_LIMIT_MS - (stateStartTime - lastCorrectClickTime)) / 1000.0);
        } else {
            long referenceTime = (playerClickProgress == 0) ? stateStartTime : lastCorrectClickTime;
            long elapsed = System.currentTimeMillis() - referenceTime;
            secondsLeft = (int) Math.ceil((CLICK_TIME_LIMIT_MS - elapsed) / 1000.0);
            timerColor = (secondsLeft <= 2) ? 0xFFFF5555 : 0xFFFFFFFF;
        }
        secondsLeft = Math.max(0, secondsLeft);

        drawScaledCenteredText(context, String.format("00:%02d", secondsLeft), screenWidth / 2, 15, timerColor, 2.0f);

        if (currentState == GameState.PLAYING && playerClickProgress == 0) {
            drawScaledCenteredText(context, "¡Comienza!", screenWidth / 2, 95, 0xFF55FF55, 1.5f);
            drawScaledCenteredText(context, "¡Repite la secuencia del patrón!", screenWidth / 2, 115, 0xFFAAAAAA, 1.0f);
        }

        int topBoxSize = 32, topBoxSpacing = 12;
        int totalTopWidth = (squaresPerRound * topBoxSize) + (Math.max(0, squaresPerRound - 1) * topBoxSpacing);
        if (totalTopWidth > screenWidth - 20) {
            float s = (float) (screenWidth - 20) / totalTopWidth;
            topBoxSize = (int) (topBoxSize * s);
            topBoxSpacing = (int) (topBoxSpacing * s);
            totalTopWidth = (squaresPerRound * topBoxSize) + (Math.max(0, squaresPerRound - 1) * topBoxSpacing);
        }
        int topStartX = (screenWidth / 2) - (totalTopWidth / 2);
        int topBoxY = 50;

        for (int i = 0; i < squaresPerRound; i++) {
            boolean shouldDraw = (currentState == GameState.SUCCESS_SCREEN) || (i < playerClickProgress);
            if (!shouldDraw) continue;

            int targetBx = topStartX + (i * (topBoxSize + topBoxSpacing));
            int drawBx = targetBx;
            float alpha = 1.0f;

            if (currentState == GameState.PLAYING) {
                long timeSinceLastClick = System.currentTimeMillis() - lastCorrectClickTime;
                int animDuration = 300;
                if (i == playerClickProgress - 1 && timeSinceLastClick < animDuration && timeSinceLastClick > 0) {
                    float p = timeSinceLastClick / (float) animDuration;
                    float easeOut = 1.0f - (float) Math.pow(1.0f - p, 3);
                    drawBx = targetBx + (int) (60 * (1.0f - easeOut));
                    alpha = p;
                }
            }

            if (alpha < 1.0f) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1, 1, 1, alpha);
            }
            context.drawTexture(getSquareTexture(currentPattern.get(i), true, false), drawBx, topBoxY, 0, 0, topBoxSize, topBoxSize, topBoxSize, topBoxSize);
            if (alpha < 1.0f) {
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }
    }

    private void drawTopRightPlayerCounter(DrawContext context, int screenWidth) {
        int boxWidth = 65, boxHeight = 24;
        int boxX = screenWidth - boxWidth - 10, boxY = 10;
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0x88000000);
        RenderSystem.enableBlend();
        context.drawTexture(PLAYER_ICON, boxX + 6, boxY + 4, 0, 0, 16, 16, 16, 16);
        RenderSystem.disableBlend();
        context.drawText(this.textRenderer, playersPassed + " / " + totalPlayers, boxX + 28, boxY + 8, 0xFFFFFFFF, true);
    }

    private void handleHoverSound(int mouseX, int mouseY) {
        int currentHovered = -1;
        if (currentState == GameState.PLAYING) {
            for (ClickableSquare sq : activeButtons) {
                if (mouseX >= sq.x && mouseX <= sq.x + sq.size && mouseY >= sq.y && mouseY <= sq.y + sq.size) {
                    currentHovered = sq.id;
                    break;
                }
            }
        }
        if (currentHovered != lastHoveredSquare) {
            if (currentHovered != -1 && this.client != null)
                this.client.getSoundManager().play(PositionedSoundInstance.master(ModSounds.SFX1, 1.0f, 5.0f));
            lastHoveredSquare = currentHovered;
        }
    }

    private void drawBaseUI(DrawContext context, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        activeButtons.clear();
        long timeSinceOpen = System.currentTimeMillis() - this.screenOpenTime;
        int yAnimOffset = 0;
        if (timeSinceOpen < 800) {
            float t = timeSinceOpen / 800.0f;
            yAnimOffset = (int) ((1.0f - (1.0f - (float) Math.pow(1.0f - t, 3))) * 250);
        }

        int squareSize = 40, spacing = 16;
        int bottomRowY = screenHeight - squareSize - 15 + yAnimOffset;
        int topRowY = bottomRowY - squareSize - spacing;
        int totalWidth = (7 * squareSize) + (6 * spacing);
        int startX = (screenWidth / 2) - (totalWidth / 2);

        boolean isPrePlaying = (currentState == GameState.PRE_MUSIC_DELAY || currentState == GameState.PLAYING_MUSIC || currentState == GameState.INTRO || currentState == GameState.SHOWING_PATTERN || currentState == GameState.WAITING_FOR_SERVER);

        RenderSystem.enableBlend();

        int[] bottomRowIds = {8, 10, 0, 1, 2, 4, 6};
        for (int i = 0; i < 7; i++) {
            int id = bottomRowIds[i];
            int x = startX + (i * (squareSize + spacing));
            boolean isClicked = currentState == GameState.PLAYING && id == lastClickedSquareId && System.currentTimeMillis() - lastCorrectClickTime < 250;
            boolean isHovered = currentState == GameState.PLAYING && mouseX >= x && mouseX <= x + squareSize && mouseY >= bottomRowY && mouseY <= bottomRowY + squareSize;
            RenderSystem.setShaderColor(1, 1, 1, isPrePlaying ? 0.3f : 1.0f);
            context.drawTexture(getSquareTexture(id, isClicked, isHovered), x, bottomRowY, 0, 0, squareSize, squareSize, squareSize, squareSize);
            activeButtons.add(new ClickableSquare(id, x, bottomRowY, squareSize));
        }

        int[] topRowIds = {7, 9, 3, 5};
        int[] topRowOffsets = {0, 1, 5, 6};
        for (int j = 0; j < 4; j++) {
            int id = topRowIds[j];
            int x = startX + (topRowOffsets[j] * (squareSize + spacing));
            boolean isClicked = currentState == GameState.PLAYING && id == lastClickedSquareId && System.currentTimeMillis() - lastCorrectClickTime < 250;
            boolean isHovered = currentState == GameState.PLAYING && mouseX >= x && mouseX <= x + squareSize && mouseY >= topRowY && mouseY <= topRowY + squareSize;
            RenderSystem.setShaderColor(1, 1, 1, isPrePlaying ? 0.3f : 1.0f);
            context.drawTexture(getSquareTexture(id, isClicked, isHovered), x, topRowY, 0, 0, squareSize, squareSize, squareSize, squareSize);
            activeButtons.add(new ClickableSquare(id, x, topRowY, squareSize));
        }

        RenderSystem.setShaderColor(1, 1, 1, isPrePlaying ? 0.3f : 1.0f);
        int heartNativeSize = 9;
        float heartScale = 2.0f;
        int heartsWidth = (5 * (heartNativeSize + 2)) - 2;
        int heartStartX = (int) (((screenWidth / 2f) / heartScale) - (heartsWidth / 2f));
        int heartsY = (int) (topRowY / heartScale) + (int) ((squareSize / 2) / heartScale) - (heartNativeSize / 2);

        context.getMatrices().push();
        context.getMatrices().scale(heartScale, heartScale, 1.0f);
        for (int i = 0; i < 5; i++) {
            int hx = heartStartX + (i * (heartNativeSize + 2));
            context.drawTexture(CONTAINER_SPRITE, hx, heartsY, 0, 0, heartNativeSize, heartNativeSize, heartNativeSize, heartNativeSize);
            if (i < lives)
                context.drawTexture(HEART_SPRITE, hx, heartsY, 0, 0, heartNativeSize, heartNativeSize, heartNativeSize, heartNativeSize);
        }
        context.getMatrices().pop();

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private void drawBreakingHeartAnimation(DrawContext context, int screenWidth, int screenHeight) {
        long elapsed = System.currentTimeMillis() - stateStartTime;
        float progress = Math.min(1.0f, elapsed / 2000.0f);
        int heartSize = 80;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2 - 40;
        float fallY = progress * progress * 100;
        float splitX = progress * 30;
        float alpha = Math.max(0.0f, 1.0f - (progress * 1.5f));
        float angleDeg = progress * 45.0f;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, alpha);

        context.getMatrices().push();
        context.getMatrices().translate(centerX - splitX - (heartSize / 4.0f), centerY + fallY + (heartSize / 2.0f), 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-angleDeg));
        context.getMatrices().translate(-(heartSize / 4.0f), -(heartSize / 2.0f), 0);
        context.drawTexture(HEART_SPRITE, 0, 0, 0.0F, 0.0F, heartSize / 2, heartSize, heartSize, heartSize);
        context.getMatrices().pop();

        context.getMatrices().push();
        context.getMatrices().translate(centerX + splitX + (heartSize / 4.0f), centerY + fallY + (heartSize / 2.0f), 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleDeg));
        context.getMatrices().translate(-(heartSize / 4.0f), -(heartSize / 2.0f), 0);
        context.drawTexture(HEART_SPRITE, 0, 0, heartSize / 2.0F, 0.0F, heartSize / 2, heartSize, heartSize, heartSize);
        context.getMatrices().pop();

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private void drawPatternSequence(DrawContext context, int screenWidth, int screenHeight) {
        long elapsed = System.currentTimeMillis() - stateStartTime;
        int index = (int) (elapsed / 2000);
        if (index < 0 || index >= currentPattern.size()) return;
        if (elapsed % 2000 >= 1800) return;
        int seqSize = 48;
        int xPos = screenWidth / 2 - seqSize / 2;
        int yPos = screenHeight / 2 - 120;
        context.drawTexture(getSquareTexture(currentPattern.get(index), false, false), xPos, yPos, 0, 0, seqSize, seqSize, seqSize, seqSize);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentState != GameState.PLAYING || button != GLFW.GLFW_MOUSE_BUTTON_LEFT)
            return super.mouseClicked(mouseX, mouseY, button);

        for (ClickableSquare sq : activeButtons) {
            if (mouseX >= sq.x && mouseX <= sq.x + sq.size && mouseY >= sq.y && mouseY <= sq.y + sq.size) {
                if (sq.id == currentPattern.get(playerClickProgress)) {
                    playerClickProgress++;
                    this.lastClickedSquareId = sq.id;
                    this.lastCorrectClickTime = System.currentTimeMillis();
                    if (this.client != null)
                        this.client.getSoundManager().play(PositionedSoundInstance.master(getSquareSound(sq.id), 1.0f, 5.0f));
                    if (playerClickProgress >= squaresPerRound)
                        changeState(GameState.SUCCESS_SCREEN);
                } else {
                    this.lives--;
                    changeState(GameState.FAILED_SCREEN);
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            OrbitalCameraSystem.applyMouseDragX(deltaX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void drawScaledCenteredText(DrawContext context, String text, int centerX, int y, int color, float scale) {
        context.getMatrices().push();
        context.getMatrices().translate(centerX, y, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawText(this.textRenderer, Text.literal(text), -this.textRenderer.getWidth(text) / 2, 0, color, true);
        context.getMatrices().pop();
    }

    private net.minecraft.sound.SoundEvent getSquareSound(int id) {
        return switch (id) {
            case 0 -> ModSounds.NUMERO_5; case 1 -> ModSounds.NUMERO_6;
            case 2 -> ModSounds.NUMERO_7; case 3 -> ModSounds.NUMERO_8;
            case 4 -> ModSounds.NUMERO_9; case 5 -> ModSounds.NUMERO_10;
            case 6 -> ModSounds.NUMERO_11; case 7 -> ModSounds.NUMERO_1;
            case 8 -> ModSounds.NUMERO_2; case 9 -> ModSounds.NUMERO_3;
            case 10 -> ModSounds.NUMERO_4; default -> ModSounds.ANUNCIAR_NUMERO;
        };
    }

    private Identifier getSquareTexture(int id, boolean clicked, boolean hovered) {
        return switch (id) {
            case 0 -> clicked ? TEX_0_CIAN_CLICK : (hovered ? TEX_0_CIAN_HOVER : TEX_0_CIAN);
            case 1 -> clicked ? TEX_1_MARRON_CLICK : (hovered ? TEX_1_MARRON_HOVER : TEX_1_MARRON);
            case 2 -> clicked ? TEX_2_GRIS_CLICK : (hovered ? TEX_2_GRIS_HOVER : TEX_2_GRIS);
            case 3 -> clicked ? TEX_3_MORADO_CLICK : (hovered ? TEX_3_MORADO_HOVER : TEX_3_MORADO);
            case 4 -> clicked ? TEX_4_VERDE_CLICK : (hovered ? TEX_4_VERDE_HOVER : TEX_4_VERDE);
            case 5 -> clicked ? TEX_5_AMARILLO_CLICK : (hovered ? TEX_5_AMARILLO_HOVER : TEX_5_AMARILLO);
            case 6 -> clicked ? TEX_6_CELESTE_CLICK : (hovered ? TEX_6_CELESTE_HOVER : TEX_6_CELESTE);
            case 7 -> clicked ? TEX_7_ROSA_CLICK : (hovered ? TEX_7_ROSA_HOVER : TEX_7_ROSA);
            case 8 -> clicked ? TEX_8_AZUL_CLICK : (hovered ? TEX_8_AZUL_HOVER : TEX_8_AZUL);
            case 9 -> clicked ? TEX_9_NARANJA_CLICK : (hovered ? TEX_9_NARANJA_HOVER : TEX_9_NARANJA);
            case 10 -> clicked ? TEX_10_ROJO_CLICK : (hovered ? TEX_10_ROJO_HOVER : TEX_10_ROJO);
            default -> clicked ? TEX_0_CIAN_CLICK : (hovered ? TEX_0_CIAN_HOVER : TEX_0_CIAN);
        };
    }

    private String getSquareName(int id) {
        return switch (id) {
            case 0 -> "Colita pal' Cielo"; case 1 -> "Nuca Torcida";
            case 2 -> "Una Vuelta"; case 3 -> "Me Sacudo";
            case 4 -> "Aleteo"; case 5 -> "Salto y me Agacho";
            case 6 -> "Robot"; case 7 -> "Pulgares Afuera";
            case 8 -> "Codos al Centro"; case 9 -> "Piernas Juntas";
            case 10 -> "Rodillas Flexionadas"; default -> "Desconocido";
        };
    }

    @Override
    public boolean shouldCloseOnEsc() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        if (this.client != null) {
            if (idleSoundInstance != null) {
                this.client.getSoundManager().stop(idleSoundInstance);
                idleSoundInstance = null;
            }
            OrbitalCameraSystem.deactivate();
            this.client.options.setPerspective(this.previousPerspective);
            this.client.options.hudHidden = false;
            GLFW.glfwSetInputMode(this.client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
        super.close();
    }

    @Override
    public boolean shouldPause() { return false; }
}