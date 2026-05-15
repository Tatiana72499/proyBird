package com.graphics.core;

import com.graphics.utils.Vec2;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

// Centraliza la lectura del teclado y la deteccion de pulsaciones simples.
public class InputManager {

    private final Window window;
    private final Map<Integer, Boolean> previousStates = new HashMap<>();

    public InputManager(Window window) {
        this.window = window;
    }

    public boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(window.getHandle(), key) == GLFW.GLFW_PRESS;
    }

    public boolean isKeyPressed(int key) {
        boolean current = isKeyDown(key);
        boolean previous = previousStates.getOrDefault(key, false);
        return current && !previous;
    }

    public boolean isLeftMouseDown() {
        return GLFW.glfwGetMouseButton(window.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    }

    public boolean isLeftMousePressed() {
        boolean current = isLeftMouseDown();
        boolean previous = previousStates.getOrDefault(GLFW.GLFW_MOUSE_BUTTON_LEFT, false);
        return current && !previous;
    }

    public Vec2 getMouseNdc() {
        return window.getCursorNdc();
    }

    // Guarda el estado actual al final del frame para detectar
    // pulsaciones simples en el frame siguiente.
    public void endFrame() {
        previousStates.clear();
        saveKey(GLFW.GLFW_KEY_ESCAPE);
        saveKey(GLFW.GLFW_KEY_ENTER);
        saveKey(GLFW.GLFW_KEY_SPACE);
        saveKey(GLFW.GLFW_KEY_1);
        saveKey(GLFW.GLFW_KEY_2);
        saveKey(GLFW.GLFW_KEY_W);
        saveKey(GLFW.GLFW_KEY_UP);
        previousStates.put(GLFW.GLFW_MOUSE_BUTTON_LEFT, isLeftMouseDown());
    }

    private void saveKey(int key) {
        previousStates.put(key, isKeyDown(key));
    }
}
