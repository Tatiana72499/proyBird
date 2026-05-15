package com.graphics.render;

import com.graphics.core.Game.GameState;
import com.graphics.entities.Bird;
import com.graphics.entities.Pipe;
import com.graphics.systems.DifficultySystem;
import java.util.List;
import org.lwjgl.opengl.GL11;

// Coordina el orden de dibujo de toda la escena.
public class Renderer {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private final HudRenderer hudRenderer = new HudRenderer();
    private float animationTime;

    public void init() {
        shapeRenderer.init();
    }

    // Orden general del render:
    // fondo, tuberias, pajaros y finalmente HUD.
    public void render(
            GameState state,
            List<Bird> birds,
            List<Pipe> pipes,
            DifficultySystem difficultySystem,
            int currentScore,
            int bestScore,
            int selectedPlayerCount) {
        animationTime += 0.016f;
        GL11.glClearColor(0.35f, 0.70f, 0.95f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        shapeRenderer.beginFrame();
        backgroundRenderer.render(shapeRenderer, animationTime);

        for (Pipe pipe : pipes) {
            pipe.render(shapeRenderer);
        }

        for (Bird bird : birds) {
            bird.render(shapeRenderer);
        }

        hudRenderer.render(shapeRenderer, state, birds, difficultySystem, animationTime, currentScore, bestScore, selectedPlayerCount);
        shapeRenderer.endFrame();
    }

    public void cleanup() {
        shapeRenderer.cleanup();
    }
}
