package com.graphics.systems;

import com.graphics.utils.Constants;

// Ajusta la dificultad segun el mejor puntaje de la partida.
public class DifficultySystem {

    private int level;
    private float pipeSpeed;
    private float spawnInterval;

    public DifficultySystem() {
        reset();
    }

    public void reset() {
        level = 1;
        pipeSpeed = Constants.INITIAL_PIPE_SPEED;
        spawnInterval = Constants.INITIAL_SPAWN_INTERVAL;
    }

    // La dificultad sube usando el mayor puntaje de los dos jugadores.
    // Asi ambos comparten el mismo ritmo de partida.
    public void update(int score1, int score2) {
        int maxScore = Math.max(score1, score2);
        level = 1 + maxScore / Constants.SCORE_PER_LEVEL;

        pipeSpeed = Constants.INITIAL_PIPE_SPEED + maxScore * Constants.PIPE_SPEED_STEP;
        if (pipeSpeed > Constants.MAX_PIPE_SPEED) {
            pipeSpeed = Constants.MAX_PIPE_SPEED;
        }

        spawnInterval = Constants.INITIAL_SPAWN_INTERVAL - maxScore * Constants.SPAWN_INTERVAL_STEP;
        if (spawnInterval < Constants.MIN_SPAWN_INTERVAL) {
            spawnInterval = Constants.MIN_SPAWN_INTERVAL;
        }
    }

    public float getPipeSpeed() {
        return pipeSpeed;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public int getLevel() {
        return level;
    }
}
