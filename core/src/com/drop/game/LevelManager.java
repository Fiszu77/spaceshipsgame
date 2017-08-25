package com.drop.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by fiszu on 21.08.2017.
 */

public class LevelManager {
    private final Pool<SimpleBoid> simpleBoidPool = new Pool<SimpleBoid>() {
        @Override
        protected SimpleBoid newObject() {
            return new SimpleBoid(0, 0);
        }
    };
    private final Pool<MotherBoid> motherBoidPool = new Pool<MotherBoid>() {
        @Override
        protected MotherBoid newObject() {
            return new MotherBoid(0, 0);
        }
    };
    private final Pool<Meteorite> meteoritesPool = new Pool<Meteorite>() {
        @Override
        protected Meteorite newObject() {
            return new Meteorite();
        }
    };
    private Array<Meteorite> meteorites;
    private Array<Obstacle> obstacles;
    private Array<Boid> boids;
    private Array<MotherBoid> motherBoids;
    private Behaviour behaviour;
    private Array<SimpleBoid> simpleBoids;
    private Array<Bullet> basicBullets;
    private Meteorite meteorite;
    private int len = 0, motherIndex, simpleIndex;
    private int meteorIndex;

    LevelManager() {
        boids = new Array<Boid>();
        basicBullets = new Array<Bullet>();
        obstacles = new Array<Obstacle>();
        motherBoids = new Array<MotherBoid>();
        simpleBoids = new Array<SimpleBoid>();
        meteorites = new Array<Meteorite>();
        behaviour = new Behaviour();
        meteorite = new Meteorite();
        for (int i = 0; i < 1; i++) {
            Meteorite meteorite = meteoritesPool.obtain();
            meteorite.init();
            obstacles.add(meteorite);
            meteorites.add(meteorite);
        }
        for (int i = 0; i < 0; i++) {
            MotherBoid boid = motherBoidPool.obtain();
            boid.init(random(MainMenuScreen.SCREEN_WIDTH), random(MainMenuScreen.SCREEN_HEIGHT));
            boids.add(boid);
            motherBoids.add(boid);
        }
        for (int i = 0; i < 20; i++) {
            SimpleBoid boid = simpleBoidPool.obtain();
            boid.init(random(MainMenuScreen.SCREEN_WIDTH), random(MainMenuScreen.SCREEN_HEIGHT));
            boids.add(boid);
            simpleBoids.add(boid);
        }
    }

    public void logic() {
        moveObstacles(obstacles);
        manageBoidPools();
        manageObstaclesPools();
        len = basicBullets.size;
        for (int i = 0; i < len; i++) {
            if (!basicBullets.get(i).isAlive()) {
                basicBullets.removeIndex(i);
                len--;
            }
        }

        for (Boid boid : boids) {
            boid.applyRules(boids);
        }
        for (Boid boid : boids) {
            for(int i = 0; i<obstacles.size; i++)
            {
                boid.isColliding(obstacles.get(i));
            }

        }
        behaviour.moveBoids(boids);

    }

    public void render(Batch batch) {
        renderObstacles(obstacles, batch);
        for (Bullet bullet : basicBullets) {
            bullet.show(batch);
            for (int i = 0; i < boids.size; i++) {
                boids.get(i).passPolygon(bullet.getPolygon());
            }
        }

        behaviour.drawBoids(boids, batch);
    }

    public void debug(OrthographicCamera camera) {
        for(Obstacle obstacle: obstacles)
        {
            obstacle.debug(camera);
        }
        for (Boid boid : boids) {
            boid.passCamera(camera);
        }
        for (Bullet bullet : basicBullets) {
            bullet.move();
            bullet.passCamera(camera);
        }
    }

    private void manageObstaclesPools() {
        len = obstacles.size;
        meteorIndex = 0;
        for (int i = 0; i < len; i++) {
            Obstacle obstacle = obstacles.get(i);
            if (!meteorites.get(meteorIndex).isAlive()) {
                meteorites.removeIndex(meteorIndex);
                meteoritesPool.free(obstacle.getMeteor());
                obstacles.removeIndex(i);
                len--;
            } else {
                motherIndex++;
            }
        }
    }

    private void manageBoidPools() {
        len = boids.size;
        simpleIndex = 0;
        motherIndex = 0;
        for (int i = 0; i < len; i++) {
            Boid boid = boids.get(i);
            if (boid.type() == "MotherBoid") {
                if (!motherBoids.get(motherIndex).isAlive()) {

                    motherBoids.removeIndex(motherIndex);

                    motherBoidPool.free(boid.getMother());
                    boids.removeIndex(i);
                    len--;
                } else {

                    motherIndex++;
                }
            }
            if (boid.type() == "SimpleBoid") {
                if (!simpleBoids.get(simpleIndex).isAlive()) {
                    simpleBoids.removeIndex(simpleIndex);
                    simpleBoidPool.free(boid.getSimple());
                    boids.removeIndex(i);
                    len--;
                } else {
                    simpleIndex++;
                }
            }
        }
    }

    private void renderObstacles(Array<Obstacle> obstacles, Batch batch)
    {
        for(Obstacle obstacle: obstacles)
        {
            obstacle.show(batch);
        }
    }
    private void moveObstacles(Array<Obstacle> obstacles)
    {
        for(Obstacle obstacle: obstacles)
        {
            obstacle.move();
        }
    }
}
