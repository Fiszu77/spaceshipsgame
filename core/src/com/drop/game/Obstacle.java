package com.drop.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fiszu on 22.08.2017.
 */

public interface Obstacle {
    boolean isAlive();
    void isHit(Polygon polygon);
    String type();
    void move();
    void show(Batch batch);
    void init();
    void dispose();
    void debug (OrthographicCamera camera);
    Vector2 getVelocity();
    Vector2 getPosition();
    Polygon getPolygon();
    Meteorite getMeteor();
    float getMass();
    void setVelocity(Vector2 newVelocity);
}
