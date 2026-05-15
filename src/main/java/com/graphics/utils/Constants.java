package com.graphics.utils;

// Valores globales configurables del proyecto.
// Tenerlos juntos facilita ajustar el juego en vivo durante el examen.
public final class Constants {

    private Constants() {
    }

    public static final String GAME_TITLE = "Flappy Bird OpenGL - Parcial 1";

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 700;

    public static final float MAX_DELTA_TIME = 0.033f;
    public static final float GROUND_Y = -0.82f;

    public static final float GRAVITY = -1.9f;
    public static final float JUMP_FORCE = 0.85f;
    public static final float MAX_FALL_SPEED = -1.8f;
    public static final float DEAD_FALL_SPEED = -1.1f;

    public static final float PLAYER_ONE_X = -0.45f;
    public static final float PLAYER_TWO_X = -0.20f;
    public static final float PLAYER_ONE_START_Y = 0.08f;
    public static final float PLAYER_TWO_START_Y = -0.08f;

    public static final float BIRD_BODY_RADIUS = 0.050f;
    public static final float BIRD_BELLY_RADIUS = 0.030f;
    public static final float BIRD_COLLISION_WIDTH = 0.105f;
    public static final float BIRD_COLLISION_HEIGHT = 0.100f;
    public static final float MAX_BIRD_ROTATION_RADIANS = 0.55f;

    public static final float PIPE_WIDTH = 0.18f;
    public static final float PIPE_GAP_HEIGHT = 0.46f;
    public static final float PIPE_SPAWN_X = 1.22f;
    public static final float MIN_GAP_CENTER = -0.30f;
    public static final float MAX_GAP_CENTER = 0.38f;

    public static final float INITIAL_PIPE_SPEED = 0.62f;
    public static final float MAX_PIPE_SPEED = 1.00f;
    public static final float PIPE_SPEED_STEP = 0.03f;

    public static final float INITIAL_SPAWN_INTERVAL = 1.45f;
    public static final float MIN_SPAWN_INTERVAL = 0.95f;
    public static final float SPAWN_INTERVAL_STEP = 0.03f;

    public static final int SCORE_PER_LEVEL = 3;
}
