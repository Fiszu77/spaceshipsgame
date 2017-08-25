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
 * Created by fiszu on 07.08.2017.
 */

public class SimpleBoid implements Boid, Pool.Poolable{
    private final Intersector intersector;
    private ShapeRenderer shapeRenderer;
    private Rectangle boid;//, boidsCollision;
    private Texture boidsTexture;
    private Vector2 boidsLocation, boidsVelocity;
    private float degrees, scaledBoidsWidth, scaledBoidsHeight;
    private float cohScl, alScl, sepScl;
    private Behaviour behavoiur;
    private TextureAtlas propAtlas,expAtlas;
    private Animation<TextureRegion> propAnim, expAnim;
    private float timePassed;
    private float[] vertices=new float[16];
    private Polygon collider;
    private boolean startExp = false, isAlive = true;
    private float hp = 100.0f, maxHp = 100.0f, mass = 20.0f;

    SimpleBoid(float startX, float startY)
    {
        shapeRenderer = new ShapeRenderer();
        collider = new Polygon();
        boidsTexture = new Texture(Gdx.files.internal("chaser2.png"));
        boid = new Rectangle();
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
        boid.width=boid.width*MainMenuScreen.SCREEN_WIDTH*0.003f;
        boid.height=boid.height*MainMenuScreen.SCREEN_WIDTH*0.003f;
        propAtlas = new TextureAtlas(Gdx.files.internal("cprop.atlas"));
        propAnim = new Animation<TextureRegion>(0.25f,propAtlas.getRegions());
        expAtlas = new TextureAtlas(Gdx.files.internal("chaserexplosion.atlas"));
        expAnim = new Animation<TextureRegion>(0.1f, expAtlas.getRegions());
        intersector = new Intersector();
        timePassed = 0.0f;

    }
    @Override
    public void init(float startX,float startY)
    {
        boidsVelocity = new Vector2();
        boidsLocation = new Vector2(startX,startY);
        boid.x = boidsLocation.x;
        boid.y = boidsLocation.y;
        //boidsCollision.width = boid.width*0.6f;
        //boidsCollision.height = boid.height*11.0f/16.0f;
        vertices[0]=0;
        vertices[1]=0;
        vertices[2]=boid.width*0.75f;
        vertices[3]=0;
        vertices[4]=boid.width*0.75f;
        vertices[5]=boid.height*5.0f/16.0f;
        vertices[6]=boid.width*0.5f;
        vertices[7]=boid.height*7.0f/16.0f;
        vertices[8]=boid.width/2;
        vertices[9]=boid.height*11.0f/16.0f;
        vertices[10]=boid.width*4.0f/16.0f;
        vertices[11]=boid.height*11.0f/16.0f;
        vertices[12]=boid.width*4.0f/16.0f;
        vertices[13]=boid.height*7.0f/16.0f;
        vertices[14]=0;
        vertices[15]=boid.height*5.0f/16.0f;

        collider.setVertices(vertices);
        collider.setOrigin(boid.width*0.75f/2, boid.height*10.0f/32);
        timePassed = 0.0f;
        startExp = false;
        isAlive = true;
    }

    @Override
    public String type() {
        return "SimpleBoid";
    }

    @Override
    public SimpleBoid getSimple() {
        return this;
    }

    @Override
    public MotherBoid getMother() {
        return null;
    }

    @Override
    public void collideWithObstacle(Obstacle obstacle) {
        Vector2 unitNormal = new Vector2().set(getLocationVector()).sub(obstacle.getPosition()).nor();
        Vector2 unitTangent = new Vector2(-unitNormal.y, unitNormal.x);
        float v1NormalBefore =unitNormal.dot(getVelocityVector());
        float v1Tangent = unitTangent.dot(getVelocityVector());
        float v2NormalBefore = unitNormal.dot(obstacle.getVelocity());
        float v2Tangent = unitTangent.dot(obstacle.getVelocity());
        float v1NormalAfter = (v1NormalBefore*(mass-obstacle.getMass())+ 2*obstacle.getMass()*v2NormalBefore)/(mass+obstacle.getMass());
        float v2NormalAfter = (v2NormalBefore*(obstacle.getMass()-mass)+ 2*mass*v1NormalBefore)/(mass+obstacle.getMass());
        System.out.println(unitTangent);
        Vector2 v1TangentVector = new Vector2(unitTangent).scl(v1Tangent);
        Vector2 v2TangentVector = unitTangent.scl(v2Tangent);
        Vector2 v1NormalVector = new Vector2(unitNormal).scl(v1NormalAfter);
        Vector2 v2NormalVector = unitNormal.scl(v2NormalAfter);
        Vector2 v1After = v1TangentVector.add(v1NormalVector);
        Vector2 v2After = v2TangentVector.add(v2NormalVector);
        boidsVelocity.set(v1After.scl(1.0f/mass).scl(scl));
        obstacle.setVelocity(v2After.scl(1.0f));
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
        acc.scl(0.013f);
        boidsVelocity.add(acc.limit(10.0f));
    }

    @Override
    public void move() {
        boidsLocation.add(boidsVelocity);
        boid.x = boidsLocation.x;
        boid.y = boidsLocation.y;
        if(boidsLocation.x>MainMenuScreen.SCREEN_WIDTH)
            boidsLocation.x=0;
        if(boidsLocation.x<0)
            boidsLocation.x=MainMenuScreen.SCREEN_WIDTH;
        if(boidsLocation.y>MainMenuScreen.SCREEN_HEIGHT)
            boidsLocation.y=0;
        if(boidsLocation.y<0)
            boidsLocation.y=MainMenuScreen.SCREEN_HEIGHT;

        collider.setPosition(boid.x-boid.width*3.0f/8.0f,boid.y-boid.height*5.0f/16.0f);
        //collider.setRotation(degrees-90.0f);


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
            batch.draw(propAnim.getKeyFrame(timePassed, true), boid.x - boid.width / 2, boid.y - boid.height * 1.32f, boid.width / 2, boid.height * 1.32f, boid.width, boid.height, 1.0f, 1.0f, degrees - 90.0f);
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
        boidsTexture.dispose();
        expAtlas.dispose();
        propAtlas.dispose();
    }

    @Override
    public void applyRules(Array<Boid> boids) {
        this.addVelocity(behavoiur.cohesion(boids, this).scl(cohScl));
        this.addVelocity(behavoiur.alignment(boids, this).scl(alScl));
        this.addVelocity(behavoiur.separation(boids, this).scl(sepScl));
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

    }

    @Override
    public void passPolygon(Polygon polygon) {
        if(intersector.overlapConvexPolygons(polygon,collider))
            kill();
    }


    @Override
    public void passCamera(Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();
    }

    @Override
    public boolean isColliding(Obstacle obstacle) {
        if(intersector.overlapConvexPolygons(collider,obstacle.getPolygon()))
        {
            collideWithObstacle(obstacle);
            return true;
        }
    return false;
    }


    @Override
    public void kill() {
        startExp = true;
        timePassed = 0.0f;
    }

    @Override
    public void reset() {

    }
}
