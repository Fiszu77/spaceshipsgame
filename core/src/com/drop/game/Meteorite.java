package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.drop.game.MainMenuScreen.scl;

/**
 * Created by fiszu on 22.08.2017.
 */

public class Meteorite implements Obstacle, Pool.Poolable{
    private  Vector2 meteorLocation;
    private ShapeRenderer shapeRenderer;
    private Vector2 meteorVelocity;
    private  Texture meteoriteTexture;
    private  Polygon collider;
    private Rectangle meteorite;
    private Intersector intersector;
    private float mass = 10.0f;
    private float[] vertices=new float[22];

    Meteorite()
    {
        shapeRenderer = new ShapeRenderer();
        meteorite = new Rectangle();
        meteorite.width = 32 * MainMenuScreen.SCREEN_WIDTH*0.003f;
        meteorite.height = 32 * MainMenuScreen.SCREEN_WIDTH*0.003f;
        collider = new Polygon();
        meteoriteTexture = new Texture(Gdx.files.internal("meteorite.png"));
        meteorLocation = new Vector2(random(MainMenuScreen.SCREEN_WIDTH),MainMenuScreen.SCREEN_HEIGHT+meteorite.height*4);
        meteorVelocity = new Vector2(random(MainMenuScreen.SCREEN_WIDTH),0.0f).sub(meteorLocation);
        meteorVelocity.nor().scl(1f);
        intersector = new Intersector();
        vertices[0]=meteorite.width*1/32;
        vertices[1]=meteorite.height*13/32;
        vertices[2]=meteorite.width*6/32;
        vertices[3]=meteorite.height*5/32;
        vertices[4]=meteorite.width*13/32;
        vertices[5]=meteorite.height*0/32;
        vertices[6]=meteorite.width*20/32;
        vertices[7]=meteorite.height*0/32;
        vertices[8]=meteorite.width*28/32;
        vertices[9]=meteorite.height*7/32;
        vertices[10]=meteorite.width*32/32;
        vertices[11]=meteorite.height*18/32;
        vertices[12]=meteorite.width*26/32;
        vertices[13]=meteorite.height*28/32;
        vertices[14]=meteorite.width*20/32;
        vertices[15]=meteorite.height*32/32;
        vertices[16]=meteorite.width*14/32;
        vertices[17]=meteorite.height*32/32;
        vertices[18]=meteorite.width*7/32;
        vertices[19]=meteorite.height*27/32;
        vertices[20]=meteorite.width*1/32;
        vertices[21]=meteorite.height*20/32;
        collider.setVertices(vertices);

    }

    @Override
    public void init() {
        meteorLocation = new Vector2(random(Gdx.graphics.getWidth()),Gdx.graphics.getHeight()+meteorite.height*4);
        meteorLocation = new Vector2(100.0f,100.0f);
        //meteorVelocity = new Vector2(random(Gdx.graphics.getWidth()),0).sub(meteorLocation);
        meteorVelocity = new Vector2(10.0f,10.0f);
        meteorVelocity.scl(scl);
        vertices[0]=meteorite.width*1/32;
        vertices[1]=meteorite.height*13/32;
        vertices[2]=meteorite.width*6/32;
        vertices[3]=meteorite.height*5/32;
        vertices[4]=meteorite.width*13/32;
        vertices[5]=meteorite.height*0/32;
        vertices[6]=meteorite.width*20/32;
        vertices[7]=meteorite.height*0/32;
        vertices[8]=meteorite.width*28/32;
        vertices[9]=meteorite.height*7/32;
        vertices[10]=meteorite.width*32/32;
        vertices[11]=meteorite.height*18/32;
        vertices[12]=meteorite.width*26/32;
        vertices[13]=meteorite.height*28/32;
        vertices[14]=meteorite.width*20/32;
        vertices[15]=meteorite.height*32/32;
        vertices[16]=meteorite.width*14/32;
        vertices[17]=meteorite.height*32/32;
        vertices[18]=meteorite.width*7/32;
        vertices[19]=meteorite.height*27/32;
        vertices[20]=meteorite.width*1/32;
        vertices[21]=meteorite.height*20/32;
        collider.setVertices(vertices);
    }

    @Override
    public void dispose() {
        meteoriteTexture.dispose();
    }

    @Override
    public void debug(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();
    }

    @Override
    public Vector2 getVelocity() {
        return meteorVelocity;
    }

    @Override
    public Polygon getPolygon() {
        return collider;
    }

    @Override
    public Meteorite getMeteor() {
        return this;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public Vector2 getPosition()
    {
        return meteorLocation;
    }

    @Override
    public void setVelocity(Vector2 newVelocity) {
        meteorVelocity.set(newVelocity);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void isHit(Polygon polygon) {

    }

    @Override
    public String type() {
        return "Meteorite";
    }

    @Override
    public void move() {
        meteorLocation.add(new Vector2(meteorVelocity).scl(scl).scl(1.0f/mass));
        collider.setPosition(meteorLocation.x-meteorite.width/2,meteorLocation.y-meteorite.height/2);
    }

    @Override
    public void show(Batch batch) {
        batch.draw(meteoriteTexture,meteorLocation.x-meteorite.width/2,meteorLocation.y-meteorite.height/2,meteorite.width,meteorite.height);
    }

    @Override
    public void reset() {


    }
}
