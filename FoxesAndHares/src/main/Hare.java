package main;

import engine.Viewport;
import extensions.Vector2d;

import java.awt.*;

public class Hare extends Animal {
    private Fox chaser;

    @Override
    protected void setup() {
        super.setup();

        chaser = null;
        color = Color.green;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(color);
        int radius = 5;

        Dimension screenPos = Viewport.worldToScreenPoint(position).toDimension();
        g.fillOval(screenPos.width - radius, screenPos.height - radius, 2*radius, 2*radius);
    }

    class HareMovementController extends Animal.AnimalMovementController {
        @Override
        protected void SetDirection() {
            if(isIdle) {
                // call a proper method
            }
            else {
                Run();
            }
        }

        private void Run() {
            direction.setX(Hare.this.position.getX() - chaser.position.getX());
            direction.setY(Hare.this.position.getY() - chaser.position.getY());
        }
    }
}