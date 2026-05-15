package com.graphics.systems;

import com.graphics.entities.Bird;
import com.graphics.entities.Pipe;
import com.graphics.utils.Constants;
import java.util.List;

// Detecta colisiones con techo, suelo y tuberias.
public class CollisionSystem {

    // La colision se revisa con cajas simples:
    // si el pajaro toca techo, suelo o una tuberia, solo ese jugador muere.
    public void updateBirdStates(List<Bird> birds, List<Pipe> pipes) {
        for (Bird bird : birds) {
            if (!bird.isEnabled() || !bird.isAlive()) {
                continue;
            }

            if (bird.getTop() >= 1.0f || bird.getBottom() <= Constants.GROUND_Y) {
                bird.kill();
                continue;
            }

            for (Pipe pipe : pipes) {
                if (pipe.collidesWith(bird)) {
                    bird.kill();
                    break;
                }
            }
        }
    }

    public boolean areAllBirdsDead(List<Bird> birds) {
        for (Bird bird : birds) {
            if (bird.isEnabled() && bird.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
