package com.graphics.systems;

import com.graphics.entities.Pipe;
import com.graphics.utils.Constants;
import java.util.List;
import java.util.Random;

// Genera tuberias usando un temporizador simple.
public class PipeSpawner {

    private final Random random = new Random();
    private float timer;

    public void reset() {
        timer = 0.0f;
    }

    // Acumula tiempo y crea una nueva tuberia cuando se cumple el intervalo.
    public void update(float deltaTime, float spawnInterval, List<Pipe> pipes) {
        timer += deltaTime;
        if (timer >= spawnInterval) {
            timer = 0.0f;
            pipes.add(createPipe());
        }
    }

    // El centro del hueco se elige al azar dentro de un rango jugable.
    private Pipe createPipe() {
        float gapY = Constants.MIN_GAP_CENTER
                + random.nextFloat() * (Constants.MAX_GAP_CENTER - Constants.MIN_GAP_CENTER);
        return new Pipe(Constants.PIPE_SPAWN_X, gapY, Constants.PIPE_WIDTH, Constants.PIPE_GAP_HEIGHT);
    }
}
