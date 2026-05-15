package com.graphics;

import com.graphics.core.Game;

// Punto de entrada del proyecto.
// La logica completa del juego vive en Game para mantener el main limpio.
public class AppFlappyBird {

    public static void main(String[] args) {
        new Game().run();
    }
}
