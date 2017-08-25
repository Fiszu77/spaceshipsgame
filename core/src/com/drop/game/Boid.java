package com.drop.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fiszu on 07.08.2017.
 */

public interface Boid {
    void addVelocity(Vector2 velocity);

    void addVelocity(float x, float y);

    void move();

    Vector2 getLocationVector();

    Vector2 getVelocityVector();

    void show(Batch batch);

    void dispose();

    void applyRules(Array<Boid> boids);

    void follow(Vector2 desired);

    boolean isAlive();

    void passPoint(Vector2 point);

    void passPolygon(Polygon polygon);

    void passCamera(Camera camera);

    boolean isColliding(Obstacle obstacle);

    void kill();

    void init(float startX, float startY);

    String type();

    SimpleBoid getSimple();

    MotherBoid getMother();

    void collideWithObstacle(Obstacle obstacle);
}
