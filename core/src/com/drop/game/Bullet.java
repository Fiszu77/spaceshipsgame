package com.drop.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fiszu on 11.08.2017.
 */

public interface Bullet {

    public void addVelocity(Vector2 velocity);
    public void addVelocity(float x, float y);

    public void move();

    public Vector2 getLocationVector();

    public Vector2 getVelocityVector();

    public void show(Batch batch);

    public void dispose();

    public void follow(Vector2 desired);

    public boolean isAlive();

    public float getPower();

    public void passCamera(Camera camera);

    public Polygon getPolygon();

    public void kill();
    public void init(float startX, float startY);
}
