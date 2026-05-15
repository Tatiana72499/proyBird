package com.graphics.core;

import com.graphics.entities.Bird;
import com.graphics.entities.Pipe;
import com.graphics.render.Renderer;
import com.graphics.systems.CollisionSystem;
import com.graphics.systems.DifficultySystem;
import com.graphics.systems.PipeSpawner;
import com.graphics.utils.Color;
import com.graphics.utils.Constants;
import com.graphics.utils.Vec2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.glfw.GLFW;

// Controla el ciclo principal del juego:
// input -> actualizacion -> render -> swap buffers.
public class Game {

    public enum GameState {
        START,
        PLAYER_SELECT,
        PLAYING,
        GAME_OVER
    }

    private Window window;
    private InputManager inputManager;
    private Renderer renderer;
    private MusicPlayer musicPlayer;
    private DifficultySystem difficultySystem;
    private PipeSpawner pipeSpawner;
    private CollisionSystem collisionSystem;

    private final List<Bird> birds = new ArrayList<>();
    private final List<Pipe> pipes = new ArrayList<>();
    private GameState state = GameState.START;
    private int selectedPlayerCount = 2;
    private int bestScore;

    public void run() {
        init();
        resetGame();
        loop();
        cleanup();
    }

    public void init() {
        window = new Window(Constants.GAME_TITLE);
        window.init();
        inputManager = new InputManager(window);
        renderer = new Renderer();
        renderer.init();
        musicPlayer = new MusicPlayer();
        musicPlayer.init();
        difficultySystem = new DifficultySystem();
        pipeSpawner = new PipeSpawner();
        collisionSystem = new CollisionSystem();
        createPlayers();
        changeState(GameState.START);
    }

    private void createPlayers() {
        birds.clear();
        birds.add(new Bird(
                "J1",
                Constants.PLAYER_ONE_X,
                Constants.PLAYER_ONE_START_Y,
                new Color(0.98f, 0.82f, 0.20f),
                GLFW.GLFW_KEY_SPACE));
        birds.add(new Bird(
                "J2",
                Constants.PLAYER_TWO_X,
                Constants.PLAYER_TWO_START_Y,
                new Color(0.30f, 0.88f, 0.98f),
                GLFW.GLFW_KEY_W));
    }

    // Reinicia la partida dejando la escena lista para volver a jugar.
    public void resetGame() {
        pipes.clear();
        difficultySystem.reset();
        pipeSpawner.reset();
        for (Bird bird : birds) {
            bird.reset();
        }
        configurePlayers(2);
        changeState(GameState.START);
    }

    private void configurePlayers(int playerCount) {
        selectedPlayerCount = playerCount;
        birds.get(0).setEnabled(true);
        birds.get(1).setEnabled(playerCount == 2);
    }

    private void loop() {
        float lastTime = (float) GLFW.glfwGetTime();
        while (!window.shouldClose()) {
            // Primero leemos eventos de GLFW para trabajar con el estado actual del teclado.
            window.pollEvents();

            float currentTime = (float) GLFW.glfwGetTime();
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            if (deltaTime > Constants.MAX_DELTA_TIME) {
                deltaTime = Constants.MAX_DELTA_TIME;
            }

            processInput();
            update(deltaTime);
            render();

            window.swapBuffers();
            inputManager.endFrame();
        }
    }

    // Procesa las teclas del frame.
    // Se usan pulsaciones simples para evitar varios saltos con una sola presion.
    public void processInput() {
        if (inputManager.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            window.requestClose();
        }

        if (inputManager.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
            if (state == GameState.START || state == GameState.GAME_OVER) {
                resetGame();
                changeState(GameState.PLAYER_SELECT);
            }
        }

        if (state == GameState.START) {
            boolean player1Jump = playerPressedJump(birds.get(0));
            boolean player2Jump = playerPressedJump(birds.get(1));
            if (player1Jump || player2Jump || clickedStartButton()) {
                changeState(GameState.PLAYER_SELECT);
            }
            return;
        }

        if (state == GameState.PLAYER_SELECT) {
            if (inputManager.isKeyPressed(GLFW.GLFW_KEY_1) || clickedPlayerButton(1)) {
                startGameWithPlayers(1);
            } else if (inputManager.isKeyPressed(GLFW.GLFW_KEY_2) || clickedPlayerButton(2)) {
                startGameWithPlayers(2);
            } else if (inputManager.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                changeState(GameState.START);
            }
            return;
        }

        if (state == GameState.GAME_OVER) {
            if (clickedReplayButton()) {
                startGameWithPlayers(selectedPlayerCount);
            } else if (clickedModeButton()) {
                resetGame();
                changeState(GameState.PLAYER_SELECT);
            }
            return;
        }

        if (state != GameState.PLAYING) {
            return;
        }

        for (Bird bird : birds) {
            if (bird.isEnabled() && bird.isAlive() && playerPressedJump(bird)) {
                bird.jump();
                musicPlayer.playJump();
            }
        }
    }

    private void startGameWithPlayers(int playerCount) {
        resetGame();
        configurePlayers(playerCount);
        changeState(GameState.PLAYING);
        // El impulso inicial hace que la partida arranque de inmediato.
        birds.get(0).jump();
        musicPlayer.playJump();
        if (playerCount == 2) {
            birds.get(1).jump();
            musicPlayer.playJump();
        }
    }

    private boolean playerPressedJump(Bird bird) {
        if (bird.getJumpKey() == GLFW.GLFW_KEY_W) {
            return inputManager.isKeyPressed(GLFW.GLFW_KEY_W) || inputManager.isKeyPressed(GLFW.GLFW_KEY_UP);
        }
        return inputManager.isKeyPressed(bird.getJumpKey());
    }

    private boolean clickedStartButton() {
        if (!inputManager.isLeftMousePressed()) {
            return false;
        }
        Vec2 mouse = inputManager.getMouseNdc();
        return isInside(mouse, 0.0f, -0.36f, 0.38f, 0.12f);
    }

    private boolean clickedPlayerButton(int playerCount) {
        if (!inputManager.isLeftMousePressed()) {
            return false;
        }
        Vec2 mouse = inputManager.getMouseNdc();
        float x = playerCount == 1 ? -0.18f : 0.18f;
        return isInside(mouse, x, -0.08f, 0.22f, 0.14f);
    }

    private boolean clickedReplayButton() {
        if (!inputManager.isLeftMousePressed()) {
            return false;
        }
        Vec2 mouse = inputManager.getMouseNdc();
        return isInside(mouse, -0.18f, -0.34f, 0.28f, 0.13f);
    }

    private boolean clickedModeButton() {
        if (!inputManager.isLeftMousePressed()) {
            return false;
        }
        Vec2 mouse = inputManager.getMouseNdc();
        return isInside(mouse, 0.18f, -0.34f, 0.28f, 0.13f);
    }

    private boolean isInside(Vec2 point, float centerX, float centerY, float width, float height) {
        return point.x >= centerX - width * 0.5f
                && point.x <= centerX + width * 0.5f
                && point.y >= centerY - height * 0.5f
                && point.y <= centerY + height * 0.5f;
    }

    // Actualiza la simulacion del juego.
    // Aqui viven la fisica del pajaro, las tuberias, las colisiones y la dificultad.
    public void update(float deltaTime) {
        if (state != GameState.PLAYING) {
            for (Bird bird : birds) {
                bird.updateIdle(deltaTime);
            }
            return;
        }

        for (Bird bird : birds) {
            bird.update(deltaTime);
        }

        difficultySystem.update(birds.get(0).getScore(), birds.get(1).getScore());
        pipeSpawner.update(deltaTime, difficultySystem.getSpawnInterval(), pipes);

        for (Pipe pipe : pipes) {
            pipe.update(deltaTime, difficultySystem.getPipeSpeed());
        }

        updateScores();
        int aliveBefore = countAliveBirds();
        collisionSystem.updateBirdStates(birds, pipes);
        if (countAliveBirds() < aliveBefore) {
            musicPlayer.playHit();
        }
        removeOffscreenPipes();

        if (collisionSystem.areAllBirdsDead(birds)) {
            changeState(GameState.GAME_OVER);
        }

        updateWindowTitle();
    }

    private void updateScores() {
        for (Pipe pipe : pipes) {
            for (int i = 0; i < birds.size(); i++) {
                Bird bird = birds.get(i);
                if (!bird.isEnabled() || !bird.isAlive()) {
                    continue;
                }
                if (pipe.tryScore(i, bird.getX())) {
                    bird.addScore();
                    bestScore = Math.max(bestScore, getCurrentScore());
                    musicPlayer.playScore();
                }
            }
        }
    }

    private void removeOffscreenPipes() {
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isOffScreen()) {
                iterator.remove();
            }
        }
    }

    public void render() {
        renderer.render(state, birds, pipes, difficultySystem, getCurrentScore(), bestScore, selectedPlayerCount);
    }

    // Toma el mejor puntaje entre los jugadores activos.
    private int getCurrentScore() {
        int currentScore = 0;
        for (Bird bird : birds) {
            if (bird.isEnabled()) {
                currentScore = Math.max(currentScore, bird.getScore());
            }
        }
        return currentScore;
    }

    private void updateWindowTitle() {
        String title = window.getBaseTitle();
        if (state == GameState.START) {
            title += " | ENTER o salto para iniciar";
        } else if (state == GameState.PLAYER_SELECT) {
            title += " | Elige 1 o 2 jugadores";
        } else if (state == GameState.GAME_OVER) {
            title += " | GAME OVER - ENTER para reiniciar";
        } else {
            title += selectedPlayerCount == 1 ? " | SPACE salta" : " | SPACE y W/UP saltan";
        }
        window.setTitle(title);
    }

    private int countAliveBirds() {
        int aliveCount = 0;
        for (Bird bird : birds) {
            if (bird.isEnabled() && bird.isAlive()) {
                aliveCount++;
            }
        }
        return aliveCount;
    }

    // Centraliza el cambio de estado para mantener sincronizados
    // titulo de ventana y musica.
    private void changeState(GameState newState) {
        if (state == newState) {
            if (state == GameState.START || state == GameState.PLAYER_SELECT) {
                musicPlayer.playMenuLoop();
            } else if (state == GameState.PLAYING) {
                musicPlayer.playGameLoop();
            }
            updateWindowTitle();
            return;
        }

        state = newState;
        if (state == GameState.START || state == GameState.PLAYER_SELECT) {
            musicPlayer.playMenuLoop();
        } else if (state == GameState.PLAYING) {
            musicPlayer.playGameLoop();
        } else if (state == GameState.GAME_OVER) {
            musicPlayer.playGameOverJingle();
        }
        updateWindowTitle();
    }

    public void cleanup() {
        renderer.cleanup();
        musicPlayer.cleanup();
        window.cleanup();
    }
}
