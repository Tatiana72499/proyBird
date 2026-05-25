package com.graphics.entities;

import com.graphics.render.ShapeRenderer;
import com.graphics.utils.Color;
import com.graphics.utils.Constants;

// Cada Bird representa a un jugador con su propia fisica y puntaje.
public class Bird {

    private final String name;
    private final float x;
    private final float startY;
    private final Color baseColor;
    private final int jumpKey;
    private final boolean reverseGravityAtEighty;

    private float y;
    private float velocityY;
    private boolean alive;
    private boolean enabled;
    private int score;
    private float wingTime;

    public Bird(String name, float x, float startY, Color baseColor, int jumpKey, boolean reverseGravityAtEighty) {
        this.name = name;
        this.x = x;
        this.startY = startY;
        this.baseColor = baseColor;
        this.jumpKey = jumpKey;
        this.reverseGravityAtEighty = reverseGravityAtEighty;
        reset();
    }

    // La fisica del pajaro es simple:
    // 1) se suma gravedad a la velocidad vertical
    // 2) se limita la velocidad maxima de caida
    // 3) se avanza la posicion con esa velocidad
    public void update(float deltaTime) {
        if (!enabled) {
            return;
        }
        wingTime += deltaTime * 8.0f;
        if (!alive) {
            velocityY += Constants.GRAVITY * deltaTime * 0.35f;
            if (velocityY < Constants.DEAD_FALL_SPEED) {
                velocityY = Constants.DEAD_FALL_SPEED;
            }
            y += velocityY * deltaTime;
            return;
        }

        if (hasReverseGravity()) {
            velocityY -= Constants.GRAVITY * deltaTime;
            if (velocityY > Constants.MAX_RISE_SPEED) {
                velocityY = Constants.MAX_RISE_SPEED;
            }
        } else {
            velocityY += Constants.GRAVITY * deltaTime;
            if (velocityY < Constants.MAX_FALL_SPEED) {
                velocityY = Constants.MAX_FALL_SPEED;
            }
        }
        y += velocityY * deltaTime;
    }

    public void updateIdle(float deltaTime) {
        if (!enabled) {
            return;
        }
        wingTime += deltaTime * 5.0f;
        y = startY + (float) Math.sin(wingTime * 0.8f) * 0.02f;
    }

    public void jump() {
        if (!enabled || !alive) {
            return;
        }
        velocityY = Constants.JUMP_FORCE;
        wingTime += 0.8f;
    }

    public void reset() {
        y = startY;
        velocityY = 0.0f;
        alive = true;
        enabled = true;
        score = 0;
        wingTime = 0.0f;
    }

    public void kill() {
        if (!alive) {
            return;
        }
        alive = false;
        velocityY = Math.min(velocityY, 0.0f);
    }

    // El pajaro se dibuja con varias figuras geometricas:
    // cuerpo, ala, pico, cola, ojo y pupila.
    // Todas se calculan alrededor del centro para que roten juntas.
    public void render(ShapeRenderer renderer) {
        if (!enabled) {
            return;
        }
        float angle = getVisualAngle();
        float wingLift = (float) Math.sin(wingTime) * 0.02f;
        Color bodyColor = alive ? baseColor : baseColor.multiply(0.45f);
        Color wingColor = alive ? bodyColor.multiply(0.82f) : bodyColor.multiply(0.70f);
        Color beakColor = new Color(1.0f, 0.55f, 0.16f);
        Color tailColor = bodyColor.multiply(0.72f);

        renderer.drawCircle(x, y, Constants.BIRD_BODY_RADIUS, bodyColor, 26);
        renderer.drawCircle(
                rotateX(-0.012f, 0.004f, angle, x),
                rotateY(-0.012f, 0.004f, angle, y),
                Constants.BIRD_BELLY_RADIUS,
                new Color(1.0f, 0.93f, 0.70f),
                20);

        float wingCenterX = rotateX(-0.016f, wingLift - 0.004f, angle, x);
        float wingCenterY = rotateY(-0.016f, wingLift - 0.004f, angle, y);
        renderer.drawTriangle(
                rotateX(-0.008f, 0.020f + wingLift, angle, wingCenterX),
                rotateY(-0.008f, 0.020f + wingLift, angle, wingCenterY),
                rotateX(-0.040f, -0.004f, angle, wingCenterX),
                rotateY(-0.040f, -0.004f, angle, wingCenterY),
                rotateX(0.010f, -0.024f, angle, wingCenterX),
                rotateY(0.010f, -0.024f, angle, wingCenterY),
                wingColor);

        renderer.drawTriangle(
                rotateX(0.040f, 0.005f, angle, x),
                rotateY(0.040f, 0.005f, angle, y),
                rotateX(0.080f, 0.020f, angle, x),
                rotateY(0.080f, 0.020f, angle, y),
                rotateX(0.080f, -0.010f, angle, x),
                rotateY(0.080f, -0.010f, angle, y),
                beakColor);

        renderer.drawTriangle(
                rotateX(-0.050f, 0.012f, angle, x),
                rotateY(-0.050f, 0.012f, angle, y),
                rotateX(-0.085f, 0.034f, angle, x),
                rotateY(-0.085f, 0.034f, angle, y),
                rotateX(-0.078f, -0.004f, angle, x),
                rotateY(-0.078f, -0.004f, angle, y),
                tailColor);

        float eyeX = rotateX(0.018f, 0.020f, angle, x);
        float eyeY = rotateY(0.018f, 0.020f, angle, y);
        renderer.drawCircle(eyeX, eyeY, 0.012f, new Color(1.0f, 1.0f, 1.0f), 16);
        renderer.drawCircle(
                rotateX(0.004f, 0.0f, angle, eyeX),
                rotateY(0.004f, 0.0f, angle, eyeY),
                0.005f,
                new Color(0.12f, 0.12f, 0.12f),
                12);
    }

    private float rotateX(float localX, float localY, float angle, float originX) {
        return originX + localX * (float) Math.cos(angle) - localY * (float) Math.sin(angle);
    }

    private float rotateY(float localX, float localY, float angle, float originY) {
        return originY + localX * (float) Math.sin(angle) + localY * (float) Math.cos(angle);
    }

    public float getLeft() {
        return x - Constants.BIRD_COLLISION_WIDTH * 0.5f;
    }

    public float getRight() {
        return x + Constants.BIRD_COLLISION_WIDTH * 0.5f;
    }

    public float getTop() {
        return y + Constants.BIRD_COLLISION_HEIGHT * 0.5f;
    }

    public float getBottom() {
        return y - Constants.BIRD_COLLISION_HEIGHT * 0.5f;
    }

    // Convierte la velocidad vertical en una inclinacion visual.
    public float getVisualAngle() {
        float normalized = velocityY / Constants.JUMP_FORCE;
        normalized = Math.max(-1.0f, Math.min(1.0f, normalized));
        return normalized * Constants.MAX_BIRD_ROTATION_RADIANS;
    }

    public boolean hasReverseGravity() {
        return reverseGravityAtEighty && score >= Constants.REVERSE_GRAVITY_SCORE;
    }

    public void addScore() {
        score++;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getScore() {
        return score;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public int getJumpKey() {
        return jumpKey;
    }
}
