package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.math.MathUtils.PI;

/**
 * Created by fiszu on 11.08.2017.
 */

public class BasicBullet implements Bullet,Pool.Poolable {
    private Rectangle bullet;
    private Texture bulletsTexture;
    private Vector2 bulletsLocation, bulletsVelocity;
    private float degrees, scaledbulletsWidth, scaledBulletsHeight;
    private Behaviour behaviour;
    private ShapeRenderer shapeRenderer;
    private Polygon collider;
    private Intersector intersector;
    private boolean startExp = false, isAlive = true;
    private float[] vertices=new float[8];
    BasicBullet()
    {
        shapeRenderer = new ShapeRenderer();
        bulletsTexture = new Texture(Gdx.files.internal("laser.png"));
        bullet = new Rectangle();
        bullet.height = 16;
        bullet.width = 3;
        //bulletsVelocity = new Vector2(random(10.0f)-5.0f,random(10.0f)-5.0f);
        bulletsVelocity = new Vector2();
        //bulletsLocation = new Vector2(random(Gdx.graphics.getWidth()),random(Gdx.graphics.getHeight()));
        bulletsLocation = new Vector2(0,0);
        bullet.x = bulletsLocation.x;
        bullet.y = bulletsLocation.y;
        behaviour = new Behaviour();
        bullet.width=bullet.width* MainMenuScreen.SCREEN_WIDTH*0.0014f;
        bullet.height=bullet.height*MainMenuScreen.SCREEN_WIDTH*0.0014f;
        vertices[0]=bullet.x;
        vertices[1]=bullet.y;
        vertices[2]=bullet.x+bullet.width;
        vertices[3]=bullet.y;
        vertices[4]=bullet.x+bullet.width;
        vertices[5]=bullet.y+bullet.height;
        vertices[6]=bullet.x;
        vertices[7]=bullet.y+bullet.height;

        collider = new Polygon();
        collider.setVertices(vertices);
    }
    @Override
    public void init(float startX, float startY) {
        bulletsVelocity = new Vector2();
        //bulletsLocation = new Vector2(random(Gdx.graphics.getWidth()),random(Gdx.graphics.getHeight()));
        bulletsLocation = new Vector2(startX,startY);
        bullet.x = bulletsLocation.x;
        bullet.y = bulletsLocation.y;
        behaviour = new Behaviour();
        vertices[0]=0;
        vertices[1]=0;
        vertices[2]=bullet.width;
        vertices[3]=0;
        vertices[4]=bullet.width;
        vertices[5]=bullet.height;
        vertices[6]=0;
        vertices[7]=bullet.height;

        collider = new Polygon();
        collider.setVertices(vertices);
    }
    @Override
    public void addVelocity(Vector2 velocity) {
        bulletsVelocity.add(velocity.limit(10.0f));
    }

    @Override
    public void addVelocity(float x, float y) {
        bulletsVelocity.add(new Vector2(x,y).nor().scl(25.0f));
    }

    @Override
    public void move() {
        bulletsLocation.add(bulletsVelocity);
        bullet.x = bulletsLocation.x;
        bullet.y = bulletsLocation.y;
        if(bulletsLocation.x>MainMenuScreen.SCREEN_WIDTH)
            isAlive=false;
        if(bulletsLocation.x<0)
            isAlive=false;
        if(bulletsLocation.y>MainMenuScreen.SCREEN_HEIGHT)
           isAlive=false;
        if(bulletsLocation.y<0)
            isAlive=false;

    }

    @Override
    public Vector2 getLocationVector() {
        return bulletsLocation;
    }

    @Override
    public Vector2 getVelocityVector() {
        return bulletsVelocity;
    }

    @Override
    public void show(Batch batch) {
        collider.setOrigin(bullet.width/2,bullet.height/2);
        collider.setPosition(bullet.x-bullet.width/2,bullet.y-bullet.height/2);
        collider.setRotation(degrees-90.0f);
        degrees = (((float)Math.atan2((double)getVelocityVector().y,getVelocityVector().x))*180.0f) /PI;
        if(degrees<0)
        {
            degrees+=360;
        }
        batch.draw(bulletsTexture, bullet.x - bullet.width / 2, bullet.y - bullet.height / 2, bullet.width / 2, bullet.height / 2, bullet.width, bullet.height, 1.0f, 1.0f, degrees - 90.0f, 0, 0, 3, 16, false, false);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void follow(Vector2 desired) {

    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public float getPower() {
        return 0;
    }

    @Override
    public void passCamera(Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 1, 1);
        //shapeRenderer.polygon(collider.getTransformedVertices());
        shapeRenderer.end();

    }

    @Override
    public Polygon getPolygon() {
        return collider;
    }

    @Override
    public void kill() {

    }


    @Override
    public void reset() {

    }
}
