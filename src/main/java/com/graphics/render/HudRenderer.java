package com.graphics.render;

import com.graphics.core.Game.GameState;
import com.graphics.entities.Bird;
import com.graphics.systems.DifficultySystem;
import com.graphics.utils.Color;
import java.util.List;

// Dibuja toda la interfaz 2D del juego:
// HUD superior, pantalla de inicio, selector de jugadores y game over.
public class HudRenderer {

    // Decide que interfaz mostrar segun el estado actual del juego.
    public void render(
            ShapeRenderer renderer,
            GameState state,
            List<Bird> birds,
            DifficultySystem difficultySystem,
            float animationTime,
            int currentScore,
            int bestScore,
            int selectedPlayerCount) {
        if (state == GameState.START) {
            renderStartScreen(renderer, birds, animationTime);
            return;
        }

        if (state == GameState.PLAYER_SELECT) {
            renderStartScreen(renderer, birds, animationTime);
            renderPlayerSelectModal(renderer, animationTime);
            return;
        }

        if (state == GameState.GAME_OVER) {
            renderGameOverScreen(renderer, currentScore, bestScore, selectedPlayerCount);
            return;
        }

        renderTopBar(renderer);
        renderPlayingHud(renderer, birds, difficultySystem.getPipeSpeed());
    }

    private void renderTopBar(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, 0.89f, 1.98f, 0.10f, new Color(0.05f, 0.13f, 0.22f, 0.72f));
        renderer.drawRect(0.0f, 0.93f, 1.98f, 0.010f, new Color(0.26f, 0.74f, 0.98f, 0.55f));
    }

    // Durante la partida se muestran los contadores individuales y la velocidad.
    private void renderPlayingHud(ShapeRenderer renderer, List<Bird> birds, float speed) {
        Bird playerOne = birds.get(0);
        Bird playerTwo = birds.get(1);
        Bird playerThree = birds.get(2);

        if (playerThree.isEnabled()) {
            renderPlayerCounter(renderer, -0.60f, 0.885f, playerOne);
            renderPlayerCounter(renderer, -0.18f, 0.885f, playerTwo);
            renderPlayerCounter(renderer, 0.24f, 0.885f, playerThree);
            renderSpeedCounter(renderer, 0.72f, 0.885f, speed);
            return;
        }

        renderPlayerCounter(renderer, -0.45f, 0.885f, playerOne);
        if (playerTwo.isEnabled()) {
            renderPlayerCounter(renderer, 0.0f, 0.885f, playerTwo);
            renderSpeedCounter(renderer, 0.52f, 0.885f, speed);
        } else {
            renderSpeedCounter(renderer, 0.18f, 0.885f, speed);
        }
    }

    // Muestra el nombre y puntaje de un jugador.
    private void renderPlayerCounter(ShapeRenderer renderer, float x, float y, Bird bird) {
        renderer.drawRect(x, y, 0.30f, 0.065f, new Color(1.0f, 1.0f, 1.0f, 0.14f));
        drawBirdBadge(renderer, x - 0.11f, y, bird.getBaseColor());
        drawPixelWord(renderer, bird.getName(), x - 0.05f, y + 0.010f, 0.012f, new Color[] {
                new Color(0.94f, 0.97f, 1.0f),
                new Color(0.94f, 0.97f, 1.0f)
        });
        drawPixelWord(renderer, Integer.toString(bird.getScore()), x + 0.07f, y + 0.010f, 0.014f, new Color[] {
                new Color(1.0f, 1.0f, 1.0f)
        });
    }

    // Muestra la velocidad actual de las tuberias para visualizar la dificultad.
    private void renderSpeedCounter(ShapeRenderer renderer, float x, float y, float speed) {
        renderer.drawRect(x, y, 0.28f, 0.065f, new Color(1.0f, 1.0f, 1.0f, 0.14f));
        renderer.drawCircle(x - 0.10f, y, 0.020f, new Color(1.0f, 0.83f, 0.26f), 16);
        renderer.drawTriangle(x - 0.10f, y + 0.010f, x - 0.12f, y - 0.012f, x - 0.08f, y - 0.012f, new Color(0.72f, 0.44f, 0.10f));
        drawPixelWord(renderer, String.format("%.2f", speed), x - 0.03f, y + 0.010f, 0.014f, new Color[] {
                new Color(1.0f, 1.0f, 1.0f),
                new Color(1.0f, 1.0f, 1.0f),
                new Color(1.0f, 1.0f, 1.0f),
                new Color(1.0f, 1.0f, 1.0f)
        });
    }

    // Pantalla de bienvenida con logo pixelado y boton visual de inicio.
    private void renderStartScreen(ShapeRenderer renderer, List<Bird> birds, float animationTime) {
        renderer.drawRect(0.0f, -0.10f, 2.0f, 2.0f, new Color(0.60f, 0.85f, 0.98f, 0.22f));

        renderStartCloudBand(renderer, animationTime);
        renderStartSkyline(renderer);
        renderStartGround(renderer);

        drawPixelWord(renderer, "FLAPPYBIRD", -0.65f, 0.34f, 0.022f, new Color[] {
                new Color(0.96f, 0.86f, 0.26f),
                new Color(0.93f, 0.44f, 0.70f),
                new Color(0.58f, 0.80f, 0.96f),
                new Color(0.53f, 0.44f, 0.92f),
                new Color(0.64f, 0.82f, 0.38f),
                new Color(0.67f, 0.47f, 0.91f),
                new Color(0.93f, 0.48f, 0.72f),
                new Color(0.98f, 0.83f, 0.26f),
                new Color(0.62f, 0.85f, 0.35f),
                new Color(0.80f, 0.94f, 0.98f)
        });

        drawLargeBirdBadge(
                renderer,
                0.58f,
                0.33f + (float) Math.sin(animationTime * 1.5f) * 0.012f,
                birds.get(0).getBaseColor());

        drawPixelWord(renderer, "START", -0.30f, -0.345f, 0.020f, new Color[] {
                new Color(1.0f, 0.25f, 0.25f),
                new Color(1.0f, 0.25f, 0.25f),
                new Color(1.0f, 0.25f, 0.25f),
                new Color(1.0f, 0.25f, 0.25f),
                new Color(1.0f, 0.25f, 0.25f)
        });
    }

    // Modal simple para elegir entre 1, 2 o 3 jugadores.
    private void renderPlayerSelectModal(ShapeRenderer renderer, float animationTime) {
        float pulse = 0.01f + (float) Math.sin(animationTime * 2.8f) * 0.006f;
        renderer.drawRect(0.0f, -0.02f, 1.02f, 0.42f, new Color(0.08f, 0.15f, 0.24f, 0.92f));
        drawPixelWord(renderer, "PLAYERS", -0.28f, 0.135f, 0.014f, new Color[] {
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f),
                new Color(0.97f, 0.99f, 0.86f)
        });

        drawSelectButton(renderer, -0.26f, -0.08f, 1, new Color(0.98f, 0.82f, 0.22f), pulse);
        drawSelectButton(renderer, 0.0f, -0.08f, 2, new Color(0.30f, 0.88f, 0.98f), pulse);
        drawSelectButton(renderer, 0.26f, -0.08f, 3, new Color(0.98f, 0.45f, 0.72f), pulse);
    }

    private void drawSelectButton(ShapeRenderer renderer, float x, float y, int number, Color color, float pulse) {
        renderer.drawRect(x, y, 0.22f, 0.14f, color);
        renderer.drawRect(x, y, 0.22f + pulse, 0.14f + pulse, new Color(color.r, color.g, color.b, 0.20f));
        renderer.drawRect(x, y, 0.18f, 0.10f, new Color(0.08f, 0.14f, 0.20f, 0.18f));
        drawPixelWord(renderer, String.valueOf(number), x - 0.028f, y + 0.030f, 0.014f, new Color[] {
                new Color(0.97f, 0.99f, 0.86f)
        });
    }

    // Pantalla final con puntaje total, mejor puntaje y botones visuales.
    private void renderGameOverScreen(ShapeRenderer renderer, int currentScore, int bestScore, int selectedPlayerCount) {
        renderer.drawRect(0.0f, 0.0f, 2.0f, 2.0f, new Color(0.04f, 0.08f, 0.12f, 0.28f));

        drawPixelWord(renderer, "GAME OVER", -0.46f, 0.34f, 0.020f, new Color[] {
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f),
                new Color(0.98f, 0.60f, 0.28f)
        });

        renderer.drawRect(0.0f, -0.02f, 0.72f, 0.32f, new Color(0.91f, 0.87f, 0.62f, 0.96f));
        renderer.drawRect(0.0f, -0.02f, 0.66f, 0.26f, new Color(0.95f, 0.92f, 0.72f, 0.92f));

        renderer.drawCircle(-0.20f, -0.03f, 0.055f, new Color(0.90f, 0.80f, 0.42f), 24);
        renderer.drawCircle(-0.20f, -0.03f, 0.040f, new Color(0.98f, 0.90f, 0.52f), 24);
        renderer.drawRect(-0.215f, -0.095f, 0.020f, 0.04f, new Color(0.92f, 0.38f, 0.24f));
        renderer.drawRect(-0.185f, -0.095f, 0.020f, 0.04f, new Color(0.92f, 0.52f, 0.24f));

        drawPixelWord(renderer, "TOTAL", 0.006f, 0.025f, 0.008f, new Color[] {
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f)
        });
        drawPixelWord(renderer, Integer.toString(currentScore), 0.25f, 0.025f, 0.008f, new Color[] {
                new Color(0.08f, 0.08f, 0.10f)
        });

        drawPixelWord(renderer, "BEST", 0.006f, -0.085f, 0.008f, new Color[] {
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f),
                new Color(0.95f, 0.50f, 0.36f)
        });
        drawPixelWord(renderer, Integer.toString(bestScore), 0.25f, -0.085f, 0.008f, new Color[] {
                new Color(0.08f, 0.08f, 0.10f)
        });

        drawReplayButton(renderer, -0.18f, -0.34f);
        drawModeButton(renderer, 0.18f, -0.34f, selectedPlayerCount);
    }

    private void drawReplayButton(ShapeRenderer renderer, float x, float y) {
        renderer.drawRect(x, y, 0.28f, 0.13f, new Color(0.96f, 0.96f, 0.96f, 0.96f));
        renderer.drawRect(x, y, 0.24f, 0.09f, new Color(0.92f, 0.92f, 0.92f, 0.70f));
        renderer.drawTriangle(
                x + 0.01f, y,
                x - 0.03f, y + 0.03f,
                x - 0.03f, y - 0.03f,
                new Color(0.10f, 0.72f, 0.30f));
    }

    // Boton derecho del game over para volver a elegir el modo de juego.
    private void drawModeButton(ShapeRenderer renderer, float x, float y, int selectedPlayerCount) {
        renderer.drawRect(x, y, 0.23f, 0.15f, new Color(0.96f, 0.96f, 0.96f, 0.96f));
        renderer.drawRect(x, y, 0.19f, 0.11f, new Color(0.92f, 0.92f, 0.92f, 0.70f));
        drawPixelWord(renderer, Integer.toString(selectedPlayerCount), x - 0.030f, y + 0.030f, 0.012f, new Color[] {
                new Color(0.94f, 0.45f, 0.34f)
        });
    }

    private void drawBirdBadge(ShapeRenderer renderer, float x, float y, Color color) {
        renderer.drawCircle(x, y, 0.030f, color, 20);
        renderer.drawTriangle(x + 0.024f, y, x + 0.050f, y + 0.010f, x + 0.050f, y - 0.008f, new Color(1.0f, 0.58f, 0.20f));
        renderer.drawCircle(x + 0.010f, y + 0.010f, 0.006f, new Color(1.0f, 1.0f, 1.0f), 12);
    }

    private void drawLargeBirdBadge(ShapeRenderer renderer, float x, float y, Color color) {
        renderer.drawCircle(x, y, 0.050f, color, 22);
        renderer.drawCircle(x - 0.010f, y + 0.004f, 0.028f, new Color(1.0f, 0.94f, 0.74f), 18);
        renderer.drawTriangle(x + 0.045f, y, x + 0.085f, y + 0.018f, x + 0.085f, y - 0.010f, new Color(1.0f, 0.58f, 0.20f));
        renderer.drawTriangle(x - 0.022f, y + 0.010f, x - 0.055f, y + 0.035f, x - 0.040f, y - 0.010f, color.multiply(0.72f));
        renderer.drawCircle(x + 0.018f, y + 0.018f, 0.010f, new Color(1.0f, 1.0f, 1.0f), 12);
        renderer.drawCircle(x + 0.022f, y + 0.018f, 0.004f, new Color(0.10f, 0.10f, 0.10f), 10);
    }

    private void renderStartCloudBand(ShapeRenderer renderer, float animationTime) {
        for (int i = 0; i < 9; i++) {
            float x = -0.96f + i * 0.24f + (float) Math.sin(animationTime * 0.5f + i) * 0.012f;
            drawCloud(renderer, x, -0.02f, 0.90f);
        }
    }

    private void drawCloud(ShapeRenderer renderer, float x, float y, float scale) {
        
    }

    private void renderStartSkyline(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, -0.46f, 2.0f, 0.40f, new Color(0.96f, 0.99f, 0.99f, 0.65f));
        float[] xs = {-0.86f, -0.70f, -0.52f, -0.35f, -0.12f, 0.10f, 0.32f, 0.56f, 0.78f};
        float[] widths = {0.12f, 0.10f, 0.16f, 0.12f, 0.18f, 0.14f, 0.16f, 0.14f, 0.12f};
        float[] heights = {0.22f, 0.28f, 0.18f, 0.36f, 0.24f, 0.32f, 0.20f, 0.26f, 0.18f};

        for (int i = 0; i < xs.length; i++) {
            float y = -0.62f + heights[i] * 0.5f;
            Color building = i % 2 == 0
                    ? new Color(0.71f, 0.90f, 0.94f, 0.88f)
                    : new Color(0.80f, 0.95f, 0.95f, 0.88f);
            renderer.drawRect(xs[i], y, widths[i], heights[i], building);
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 2; col++) {
                    float wx = xs[i] - widths[i] * 0.20f + col * widths[i] * 0.22f;
                    float wy = y + heights[i] * 0.24f - row * 0.06f;
                    renderer.drawRect(wx, wy, 0.018f, 0.022f, new Color(0.93f, 1.0f, 0.86f, 0.70f));
                }
            }
        }
    }

    private void renderStartGround(ShapeRenderer renderer) {
        renderer.drawRect(0.0f, -0.84f, 2.0f, 0.08f, new Color(0.38f, 0.80f, 0.22f));
        renderer.drawRect(0.0f, -0.84f, 2.0f, 0.03f, new Color(0.24f, 0.63f, 0.14f));
        for (int i = 0; i < 18; i++) {
            float x = -0.94f + i * 0.11f;
            renderer.drawRect(
                    x,
                    -0.84f,
                    0.06f,
                    0.045f,
                    i % 2 == 0 ? new Color(0.64f, 0.91f, 0.38f) : new Color(0.46f, 0.78f, 0.22f));
        }
        renderer.drawRect(0.0f, -0.93f, 2.0f, 0.14f, new Color(0.88f, 0.83f, 0.56f));
    }

    // Dibuja texto pixelado a partir de patrones binarios por letra.
    private void drawPixelWord(ShapeRenderer renderer, String word, float startX, float baselineY, float pixelSize, Color[] colors) {
        float x = startX;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter == ' ') {
                x += pixelSize * 3.0f;
                continue;
            }
            drawPixelLetter(renderer, letter, x, baselineY, pixelSize, colors[Math.min(i, colors.length - 1)]);
            x += pixelSize * 6.0f;
        }
    }

    private void drawPixelLetter(ShapeRenderer renderer, char letter, float x, float y, float pixelSize, Color color) {
        String[] pattern = getLetterPattern(letter);
        Color shadow = new Color(0.18f, 0.18f, 0.22f, 0.85f);
        Color highlight = color.multiply(1.12f);

        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length(); col++) {
                if (pattern[row].charAt(col) != '1') {
                    continue;
                }
                float px = x + col * pixelSize;
                float py = y - row * pixelSize;
                renderer.drawRect(px + pixelSize * 0.08f, py - pixelSize * 0.08f, pixelSize * 1.02f, pixelSize * 1.02f, shadow);
                renderer.drawRect(px, py, pixelSize, pixelSize, color);
                renderer.drawRect(px - pixelSize * 0.18f, py + pixelSize * 0.18f, pixelSize * 0.30f, pixelSize * 0.30f, highlight);
            }
        }
    }

    private String[] getLetterPattern(char letter) {
        return switch (letter) {
            case 'A' -> new String[] {"01110", "10001", "10001", "11111", "10001", "10001", "10001"};
            case 'B' -> new String[] {"11110", "10001", "10001", "11110", "10001", "10001", "11110"};
            case 'D' -> new String[] {"11110", "10001", "10001", "10001", "10001", "10001", "11110"};
            case 'E' -> new String[] {"11111", "10000", "10000", "11110", "10000", "10000", "11111"};
            case 'F' -> new String[] {"11111", "10000", "10000", "11110", "10000", "10000", "10000"};
            case 'G' -> new String[] {"01110", "10001", "10000", "10111", "10001", "10001", "01110"};
            case 'I' -> new String[] {"11111", "00100", "00100", "00100", "00100", "00100", "11111"};
            case 'J' -> new String[] {"00111", "00010", "00010", "00010", "10010", "10010", "01100"};
            case 'L' -> new String[] {"10000", "10000", "10000", "10000", "10000", "10000", "11111"};
            case 'M' -> new String[] {"10001", "11011", "10101", "10101", "10001", "10001", "10001"};
            case 'O' -> new String[] {"01110", "10001", "10001", "10001", "10001", "10001", "01110"};
            case 'P' -> new String[] {"11110", "10001", "10001", "11110", "10000", "10000", "10000"};
            case 'R' -> new String[] {"11110", "10001", "10001", "11110", "10100", "10010", "10001"};
            case 'S' -> new String[] {"01111", "10000", "10000", "01110", "00001", "00001", "11110"};
            case 'T' -> new String[] {"11111", "00100", "00100", "00100", "00100", "00100", "00100"};
            case 'V' -> new String[] {"10001", "10001", "10001", "10001", "10001", "01010", "00100"};
            case 'Y' -> new String[] {"10001", "10001", "01010", "00100", "00100", "00100", "00100"};
            case '0' -> new String[] {"01110", "10001", "10011", "10101", "11001", "10001", "01110"};
            case '1' -> new String[] {"00100", "01100", "00100", "00100", "00100", "00100", "01110"};
            case '2' -> new String[] {"01110", "10001", "00001", "00010", "00100", "01000", "11111"};
            case '3' -> new String[] {"11110", "00001", "00001", "01110", "00001", "00001", "11110"};
            case '4' -> new String[] {"10010", "10010", "10010", "11111", "00010", "00010", "00010"};
            case '5' -> new String[] {"11111", "10000", "10000", "11110", "00001", "00001", "11110"};
            case '6' -> new String[] {"01110", "10000", "10000", "11110", "10001", "10001", "01110"};
            case '7' -> new String[] {"11111", "00001", "00010", "00100", "01000", "01000", "01000"};
            case '8' -> new String[] {"01110", "10001", "10001", "01110", "10001", "10001", "01110"};
            case '9' -> new String[] {"01110", "10001", "10001", "01111", "00001", "00001", "01110"};
            case '.' -> new String[] {"00000", "00000", "00000", "00000", "00000", "00110", "00110"};
            default -> new String[] {"00000", "00000", "00000", "00000", "00000", "00000", "00000"};
        };
    }
}
