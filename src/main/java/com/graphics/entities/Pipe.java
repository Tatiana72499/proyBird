package com.graphics.entities;

import com.graphics.render.ShapeRenderer;
import com.graphics.utils.Color;
import com.graphics.utils.Constants;

// Modela una tuberia compartida por ambos jugadores.
public class Pipe {

    private float x;
    private final float gapY;
    private final float width;
    private final float gapHeight;
    private boolean scoredPlayer1;
    private boolean scoredPlayer2;

    public Pipe(float x, float gapY, float width, float gapHeight) {
        this.x = x;
        this.gapY = gapY;
        this.width = width;
        this.gapHeight = gapHeight;
    }

    public void update(float deltaTime, float speed) {
        x -= speed * deltaTime;
    }

    public void render(ShapeRenderer renderer) {
        float gapTop = gapY + gapHeight * 0.5f;
        float gapBottom = gapY - gapHeight * 0.5f;
        float topHeight = 1.0f - gapTop;
        float bottomHeight = gapBottom - Constants.GROUND_Y;

        Color pipeColor = new Color(0.17f, 0.66f, 0.28f);
        Color pipeHighlight = new Color(0.40f, 0.88f, 0.44f);
        Color pipeShadow = new Color(0.10f, 0.44f, 0.18f);
        Color capColor = new Color(0.12f, 0.55f, 0.22f);
        float capHeight = 0.06f;
        float capWidth = width * 1.22f;

        if (topHeight > 0.0f) {
            float topCenterY = gapTop + topHeight * 0.5f;
            renderer.drawRect(x, topCenterY, width, topHeight, pipeColor);
            renderer.drawRect(x - width * 0.22f, topCenterY, width * 0.16f, topHeight, pipeHighlight);
            renderer.drawRect(x + width * 0.26f, topCenterY, width * 0.12f, topHeight, pipeShadow);
            renderer.drawRect(x, gapTop - capHeight * 0.5f, capWidth, capHeight, capColor);
            renderer.drawRect(x - width * 0.12f, gapTop - capHeight * 0.5f, capWidth * 0.12f, capHeight, pipeHighlight);
        }

        if (bottomHeight > 0.0f) {
            float bottomCenterY = Constants.GROUND_Y + bottomHeight * 0.5f;
            renderer.drawRect(x, bottomCenterY, width, bottomHeight, pipeColor);
            renderer.drawRect(x - width * 0.22f, bottomCenterY, width * 0.16f, bottomHeight, pipeHighlight);
            renderer.drawRect(x + width * 0.26f, bottomCenterY, width * 0.12f, bottomHeight, pipeShadow);
            renderer.drawRect(x, gapBottom + capHeight * 0.5f, capWidth, capHeight, capColor);
            renderer.drawRect(x - width * 0.12f, gapBottom + capHeight * 0.5f, capWidth * 0.12f, capHeight, pipeHighlight);
        }
    }

    public boolean isOffScreen() {
        return x + width * 0.5f < -1.3f;
    }

    // Revisa la colision contra cuerpo y tapas de la tuberia
    // para que el choque coincida con lo que se ve en pantalla.
    public boolean collidesWith(Bird bird) {
        float gapTop = gapY + gapHeight * 0.5f;
        float gapBottom = gapY - gapHeight * 0.5f;
        float capHeight = 0.06f;
        float capWidth = width * 1.22f;

        return overlapsRect(bird, x, (1.0f + gapTop) * 0.5f, width, 1.0f - gapTop)
                || overlapsRect(bird, x, gapTop - capHeight * 0.5f, capWidth, capHeight)
                || overlapsRect(bird, x, (Constants.GROUND_Y + gapBottom) * 0.5f, width, gapBottom - Constants.GROUND_Y)
                || overlapsRect(bird, x, gapBottom + capHeight * 0.5f, capWidth, capHeight);
    }

    private boolean overlapsRect(Bird bird, float rectCenterX, float rectCenterY, float rectWidth, float rectHeight) {
        if (rectWidth <= 0.0f || rectHeight <= 0.0f) {
            return false;
        }
        float rectLeft = rectCenterX - rectWidth * 0.5f;
        float rectRight = rectCenterX + rectWidth * 0.5f;
        float rectTop = rectCenterY + rectHeight * 0.5f;
        float rectBottom = rectCenterY - rectHeight * 0.5f;

        return bird.getRight() > rectLeft
                && bird.getLeft() < rectRight
                && bird.getTop() > rectBottom
                && bird.getBottom() < rectTop;
    }

    // Cada jugador puntua una sola vez por tuberia.
    public boolean tryScore(int playerIndex, float birdX) {
        if (x + width * 0.5f >= birdX) {
            return false;
        }

        if (playerIndex == 0 && !scoredPlayer1) {
            scoredPlayer1 = true;
            return true;
        }
        if (playerIndex == 1 && !scoredPlayer2) {
            scoredPlayer2 = true;
            return true;
        }
        return false;
    }
}
