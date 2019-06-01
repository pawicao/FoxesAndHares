package main;

import engine.SimulationManager;
import engine.Time;
import engine.Viewport;
import extensions.Vector2d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.lang.Math;

public class Hare extends Animal {
    HareMovement movementController = new HareMovement();

    private static int maleHares = 0;
    private static int hares = 0;
    private final static int lifespan = 13;
    private final static int minBreedAge = 2;

    @Override
    protected void setup() {
        super.setup();
        this.gender = setGender();
        ++Hare.hares;
        addBehaviour(movementController);
        color = Color.green;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(color);
        int radius = 5;

        Dimension screenPos = Viewport.worldToScreenPoint(position).toDimension();
        g.fillOval(screenPos.width - radius, screenPos.height - radius, 2*radius, 2*radius);
    }

    protected boolean setGender() {
        int tmp;
        boolean gender = new Random().nextBoolean();
        if(gender)
            tmp = Hare.maleHares;
        else
            tmp = Hare.hares - Hare.maleHares;
        if((tmp + 1)/(Hare.hares + 1) > SimulationManager.genderMaxPercentage)
            gender = !gender;
        if(gender)
            ++Hare.maleHares;
        return gender;
    }

    protected void breed() {
        List<Animal> nearHares = getVisibleHares();
        for(Animal hare : nearHares) {
            if(this.gender == hare.gender)
                continue;
            if(Math.random() <= (float)SimulationManager.hareBirthRate/100) {
                double currentTime = Time.getTime();
                Animal mother;
                if(this.gender)
                    mother = hare;
                else
                    mother = this;
                if(!this.isFertile || !hare.isFertile || currentTime - mother.lastBirthTime < Animal.fertilenessFrequency)
                    continue;
                mother.lastBirthTime = Time.getTime();
                SimulationManager.getInstance().createAnimal("Hare_" + Hare.hares, Hare.class.getName(), mother.position);
                break;
            }
        }
    }

    protected void getOlder() {
        double time = Time.getTime();
        if(time - lastBirthday >= SimulationManager.yearDuration) {
            ++age;
            switch(age) {
                case Hare.minBreedAge:
                    isFertile = true;
                    break;
                case Hare.lifespan:
                    Die();
                    break;
            }
            lastBirthday = time;
        }
    }

    public class HareMovement extends Animal.AnimalMovementController {
        private double pathFindThreshold = 4.0;

        List<Animal> visibleFoxes = new ArrayList<>();

        private double moveSpeed = 2.0;
        private double runSpeed = 4.0;

        @Override
        public void action() {
            getOlder();
            visibleFoxes = getVisibleFoxes();
            setDirection();
            move();
            if(isIdle) {
                breed();
            }
        }


        protected void move() {
            synchronized (this) {
                double speed = isIdle ? moveSpeed : runSpeed;
                Vector2d dir = direction.normalized();
                double deltaTime = Time.getDeltaTime();
                Vector2d move = dir.scaled(deltaTime).scaled(speed);
                position.add(move);
            }
        }

        private void idle() {}

        private void setDirection() {
            if (visibleFoxes.size() == 0) {
                setIdleDirection();
                isIdle = true;
            } else {
                direction = getOptimalRunDirection();
                isIdle = false;
            }
        }

        private Vector2d getOptimalRunDirection() {
            double epsilon = 1.0; //degrees
            double bestNorm = 0.0;
            Vector2d bestDir = null;

            double startAngle = 360 - (Math.toDegrees(Math.atan2(direction.y, direction.x)) - 90);

            for (double phi = 0.0; phi < 360.0; phi += epsilon) {
                double phiRad = Math.toRadians(phi + startAngle % 360.0);
                Vector2d dir = new Vector2d(Math.sin(phiRad), Math.cos(phiRad));
                Vector2d move = dir.scaled(pathFindThreshold);
                Vector2d pos = position.plus(move);

                if (!Viewport.isInView(pos)) {
                    continue;
                }

                double norm = getDistanceNorm(pos);
                if (bestDir == null || norm > bestNorm) {
                    bestNorm = norm;
                    bestDir = dir;
                }
            }

            return bestDir;
        }

        private double getDistanceNorm(Vector2d pos) {
            double sum = 0;
            for (Animal fox : visibleFoxes) {
                sum += Vector2d.distance(fox.position, pos);
            }
            return sum;
        }

        private List<Animal> getVisibleFoxes() {
            List<Animal> animals = visionController.getVisible();
            List<Animal> foxes = animals.stream()
                    .filter(s -> s instanceof Fox)
                    .collect(Collectors.toList());
            return foxes;
        }
    }

}
