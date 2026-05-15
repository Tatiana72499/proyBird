package com.graphics.render;

import com.graphics.utils.Color;
import com.graphics.utils.Constants;

// Dibuja el escenario del juego con figuras geometricas simples.
// La escena tiene varias capas para que el fondo se vea agradable.
public class BackgroundRenderer {

    // Orden de capas del fondo: cielo, sol, nubes, montanas, arbustos y suelo.
    public void render(ShapeRenderer renderer, float animationTime) {
        renderSky(renderer);
        renderSun(renderer, animationTime);
        renderClouds(renderer, animationTime);
        renderMountains(renderer);
        renderBushes(renderer);
        renderGround(renderer);
    }

    private void renderSky(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, 0.62f, 2.0f, 0.78f, new Color(0.28f, 0.66f, 0.98f));
        renderer.drawRect(0.0f, 0.18f, 2.0f, 0.72f, new Color(0.49f, 0.79f, 0.96f));
    }

    // El sol usa dos circulos: uno para brillo y otro para el centro.
    private void renderSun(ShapeRenderer renderer, float animationTime) {
        float pulse = 0.01f + (float) Math.sin(animationTime * 0.7f) * 0.004f;
        renderer.drawCircle(0.72f, 0.72f, 0.12f + pulse, new Color(1.0f, 0.84f, 0.32f, 0.30f), 26);
        renderer.drawCircle(0.72f, 0.72f, 0.08f, new Color(1.0f, 0.90f, 0.42f), 24);
    }

    // Las nubes se mueven levemente para que el fondo no se vea estatico.
    private void renderClouds(ShapeRenderer renderer, float animationTime) {
        drawCloud(renderer, -0.67f + (float) Math.sin(animationTime * 0.18f) * 0.02f, 0.72f, 1.0f);
        drawCloud(renderer, 0.12f + (float) Math.cos(animationTime * 0.15f) * 0.03f, 0.62f, 0.9f);
        drawCloud(renderer, 0.44f + (float) Math.sin(animationTime * 0.12f) * 0.02f, 0.77f, 0.72f);
    }

    private void drawCloud(ShapeRenderer renderer, float x, float y, float scale) {
        Color cloud = new Color(1.0f, 1.0f, 1.0f, 0.90f);
        renderer.drawCircle(x - 0.05f * scale, y, 0.05f * scale, cloud, 18);
        renderer.drawCircle(x, y + 0.02f * scale, 0.065f * scale, cloud, 18);
        renderer.drawCircle(x + 0.06f * scale, y, 0.05f * scale, cloud, 18);
        renderer.drawRect(x, y - 0.02f * scale, 0.17f * scale, 0.05f * scale, cloud);
    }

    // Las montanas usan varios triangulos con diferentes tonos para dar profundidad.
    private void renderMountains(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, -0.48f, 2.0f, 0.78f, new Color(0.54f, 0.77f, 0.96f, 0.55f));

        renderer.drawTriangle(
                -1.10f, Constants.GROUND_Y,
                -0.55f, 0.10f,
                0.10f, Constants.GROUND_Y,
                new Color(0.60f, 0.80f, 0.65f));

        renderer.drawTriangle(
                -0.45f, Constants.GROUND_Y,
                0.20f, 0.18f,
                0.85f, Constants.GROUND_Y,
                new Color(0.50f, 0.74f, 0.58f));

        renderer.drawTriangle(
                0.10f, Constants.GROUND_Y,
                0.75f, 0.08f,
                1.15f, Constants.GROUND_Y,
                new Color(0.61f, 0.82f, 0.67f));

        renderer.drawTriangle(
                -1.05f, Constants.GROUND_Y,
                -0.55f, 0.28f,
                0.18f, Constants.GROUND_Y,
                new Color(0.42f, 0.72f, 0.51f));

        renderer.drawTriangle(
                -0.40f, Constants.GROUND_Y,
                0.18f, 0.36f,
                0.82f, Constants.GROUND_Y,
                new Color(0.35f, 0.66f, 0.47f));

        renderer.drawTriangle(
                0.05f, Constants.GROUND_Y,
                0.70f, 0.26f,
                1.15f, Constants.GROUND_Y,
                new Color(0.45f, 0.74f, 0.53f));
    }

    private void renderBushes(ShapeRenderer renderer) {
        drawBush(renderer, -0.72f, Constants.GROUND_Y + 0.05f, 0.90f);
        drawBush(renderer, -0.12f, Constants.GROUND_Y + 0.03f, 1.05f);
        drawBush(renderer, 0.52f, Constants.GROUND_Y + 0.04f, 0.95f);
    }

    private void drawBush(ShapeRenderer renderer, float x, float y, float scale) {
        Color bush = new Color(0.24f, 0.62f, 0.24f);
        renderer.drawCircle(x - 0.06f * scale, y, 0.05f * scale, bush, 18);
        renderer.drawCircle(x, y + 0.02f * scale, 0.065f * scale, bush, 18);
        renderer.drawCircle(x + 0.07f * scale, y, 0.052f * scale, bush, 18);
    }

    // El suelo se compone de tierra, cesped y lineas diagonales decorativas.
    private void renderGround(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, Constants.GROUND_Y - 0.08f, 2.0f, 0.24f, new Color(0.83f, 0.70f, 0.34f));
        renderer.drawRect(0.0f, Constants.GROUND_Y + 0.020f, 2.0f, 0.050f, new Color(0.31f, 0.79f, 0.20f));
        renderer.drawRect(0.0f, Constants.GROUND_Y - 0.005f, 2.0f, 0.012f, new Color(0.24f, 0.60f, 0.16f));

        for (int i = 0; i < 12; i++) {
            float stripeX = -0.95f + i * 0.17f;
            renderer.drawLine(
                    stripeX,
                    Constants.GROUND_Y - 0.04f,
                    stripeX + 0.08f,
                    Constants.GROUND_Y - 0.10f,
                    0.008f,
                    new Color(0.68f, 0.54f, 0.20f));
        }
    }
}
