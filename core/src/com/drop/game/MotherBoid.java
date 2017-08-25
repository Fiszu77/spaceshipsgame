package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.drop.game.MainMenuScreen.scl;


/**
 * Created by fiszu on 010.08.2017.
 */

public class MotherBoid implements Boid, Pool.Poolable {
    private Rectangle boid, boidsCollision;
    private Texture boidsTexture;
    private Vector2 boidsLocation, boidsVelocity;
    private float degrees, scaledBoidsWidth, scaledBoidsHeight;
    private float cohScl, alScl, sepScl;
    private float[] vertices=new float[8];
    private Behaviour behavoiur;
    private TextureAtlas propAtlas,expAtlas;
    private Animation<TextureRegion> propAnim,expAnim;
    private float timePassed, mass = 40.0f, hp =200.0f, maxHp = 200.0f;
    private ShapeRenderer shapeRenderer;
    private Polygon collider;
    private Intersector intersector;
    private boolean startExp = false, isAlive = true;

    MotherBoid(float startX, float startY)
    {
        boidsTexture = new Texture(Gdx.files.internal("spaceship3.png"));
        boid = new Rectangle();
        boidsCollision = new Rectangle();
        boid.height = 16;
        boid.width = 16;
        //boidsVelocity = new Vector2(random(10.0f)-5.0f,random(10.0f)-5.0f);
        boidsVelocity = new Vector2();
        //boidsLocation = new Vector2(random(Gdx.graphics.getWidth()),random(Gdx.graphics.getHeight()));
        boidsLocation = new Vector2(startX,startY);
        boid.x = boidsLocation.x;
        boid.y = boidsLocation.y;
        behavoiur = new Behaviour();
        cohScl = 24.0f;
        alScl = 22.0f;
        sepScl = 54.0f;
        boid.width=boid.width* MainMenuScreen.SCREEN_WIDTH*0.003f;
        boid.height=boid.height*MainMenuScreen.SCREEN_WIDTH*0.003f;
        boidsCollision.width = boid.width*0.6f;
        boidsCollision.height = boid.height;
        vertices[0]=boidsCollision.x;
        vertices[1]=boidsCollision.y;
        vertices[2]=boidsCollision.x+boidsCollision.width;
        vertices[3]=boidsCollision.y;
        vertices[4]=boidsCollision.x+boidsCollision.width;
        vertices[5]=boidsCollision.y+boidsCollision.height;
        vertices[6]=boidsCollision.x;
        vertices[7]=boidsCollision.y+boidsCollision.height;

        collider = new Polygon();
        collider.setVertices(vertices);
        propAtlas = new TextureAtlas(Gdx.files.internal("prop.atlas"));
        propAnim = new Animation<TextureRegion>(0.25f,propAtlas.getRegions());
        timePassed = 0.0f;
        expAtlas = new TextureAtlas(Gdx.files.internal("explosion2.atlas"));
        expAnim = new Animation<TextureRegion>(0.10f,expAtlas.getRegions());
        shapeRenderer = new ShapeRenderer();
        intersector = new Intersector();
    }
    @Override
    public void reset() {
    }
    @Override
    public void init(float startX,float startY)
    {
        boidsVelocity = new Vector2();
        boidsLocation = new Vector2(startX,startY);
        collider = new Polygon();
        boid.x = boidsLocation.x;
        boid.y = boidsLocation.y;
        boidsCollision.x = 0;
        boidsCollision.y = 0;
        vertices[0]=boidsCollision.x;
        vertices[1]=boidsCollision.y;
        vertices[2]=boidsCollision.x+boidsCollision.width;
        vertices[3]=boidsCollision.y;
        vertices[4]=boidsCollision.x+boidsCollision.width;
        vertices[5]=boidsCollision.y+boidsCollision.height;
        vertices[6]=boidsCollision.x;
        vertices[7]=boidsCollision.y+boidsCollision.height;
        collider.setVertices(vertices);
        collider.setOrigin(boidsCollision.width/2,boidsCollision.height/2);
        timePassed = 0.0f;
        startExp = false;
        isAlive = true;
    }

    @Override
    public String type() {
        return "MotherBoid";
    }

    @Override
    public SimpleBoid getSimple() {
        return null;
    }

    @Override
    public MotherBoid getMother() {
        return this;
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle) {

    }

    public void setCohScl(float cohScl) { this.cohScl = cohScl; }

    public void setAlScl(float alScl) {
        this.alScl = alScl;
    }

    public void setSepScl(float sepScl) {
        this.sepScl = sepScl;
    }
    @Override
    public void addVelocity(Vector2 velocity) {

        boidsVelocity.add(velocity.scl(1.0f/mass).limit(10.0f).scl(scl));

    }
    @Override
    public void addVelocity(float x, float y) {
        Vector2 acc = new Vector2(x,y);
        acc.scl(0.01f);
        boidsVelocity.add(acc.limit(10.0f));
    }

    public void steer(float x, float y)
    {
        Vector2 acc = new Vector2(x,y);
        if(boidsVelocity.len()<7.0f)
        {
            acc.nor().scl(boidsVelocity.len()+0.1f);
        }
        else
        {
            acc.nor().scl(7.0f);
        }
        boidsVelocity.set(acc);
    }

    public void slowDown()
    {
        boidsVelocity.sub(new Vector2(boidsVelocity.x, boidsVelocity.y).scl(0.02f));
    }

    @Override
    public void move() {
        boidsLocation.add(boidsVelocity);
        boid.x = boidsLocation.x;
        boid.y = boidsLocation.y;
        boidsCollision.x = boid.x-boid.width/2+boid.width*0.2f;
        boidsCollision.y = boid.y-boid.height/2;
        if(boidsLocation.x>MainMenuScreen.SCREEN_WIDTH)
            boidsLocation.x=0;
        if(boidsLocation.x<0)
            boidsLocation.x=MainMenuScreen.SCREEN_WIDTH;
        if(boidsLocation.y>MainMenuScreen.SCREEN_HEIGHT)
            boidsLocation.y=0;
        if(boidsLocation.y<0)
            boidsLocation.y=MainMenuScreen.SCREEN_HEIGHT;

        collider.setPosition(boidsCollision.x,boidsCollision.y);
        collider.setRotation(degrees-90.0f);


    }
    @Override
    public void passCamera(Camera camera){

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();
    }

    @Override
    public boolean isColliding(Obstacle obstacle) {
    return false;

    }


    @Override
    public void kill() {
        startExp = true;

        timePassed=0;
    }

    @Override
    public Vector2 getLocationVector() {
        return boidsLocation;
    }

    @Override
    public Vector2 getVelocityVector(){
        return boidsVelocity;
    }

    @Override
    public void show(Batch batch) {
        propAnim.setFrameDuration(1.0f/getVelocityVector().len());
        degrees = (((float)Math.atan2((double)getVelocityVector().y,getVelocityVector().x))*180.0f) /PI;
        //System.out.println("przed"+degrees);
        if(degrees<0)
        {
            degrees+=360;
        }
        //System.out.println(degrees);

        timePassed += Gdx.graphics.getDeltaTime();
        if(!startExp) {
            batch.draw(propAnim.getKeyFrame(timePassed, true), boid.x - boid.width / 2, boid.y - boid.height * 1.5f, boid.width / 2, boid.height * 1.5f, boid.width, boid.height, 1.0f, 1.0f, degrees - 90.0f);

            batch.draw(boidsTexture, boid.x - boid.width / 2, boid.y - boid.height / 2, boid.width / 2, boid.height / 2, boid.width, boid.height, 1.0f, 1.0f, degrees - 90.0f, 0, 0, 16, 16, false, false);
        }
        else
        {

            if(!expAnim.isAnimationFinished(timePassed)) {
                batch.draw(expAnim.getKeyFrame(timePassed, true), boid.x - boid.width / 2, boid.y - boid.height / 2, boid.width / 2, boid.height / 2, boid.width, boid.height, 1.0f, 1.0f, degrees - 90.0f);
                boidsVelocity.limit(2.0f);
            }
            else
                isAlive=false;
        }
    }

    @Override
    public void dispose() {
        expAtlas.dispose();
        propAtlas.dispose();
        boidsTexture.dispose();
    }

    @Override
    public void applyRules(Array<Boid> boids) {
        //this.addVelocity(behavoiur.cohesion(boids, this).scl(cohScl));
        //this.addVelocity(behavoiur.alignment(boids, this).scl(alScl));
        //this.addVelocity(behavoiur.separation(boids, this).scl(sepScl));
    }

    @Override
    public void follow(Vector2 desired) {
        this.addVelocity(behavoiur.follow(desired, this));
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void passPoint(Vector2 point) {

            if(intersector.isPointInPolygon(collider.getTransformedVertices(),0,8,point.x,point.y))
            {
                kill();
            }
    }

    @Override
    public void passPolygon(Polygon polygon) {
        if(intersector.overlapConvexPolygons(polygon,collider))
        kill();
    }


}
