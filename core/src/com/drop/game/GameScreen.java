package com.drop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;


public class GameScreen implements Screen {
    final MyGdxGame game;
    private Intersector intersector;
    private FPSLogger fpsLogger;
   // private Array<Boid> boids;
    //private Array<MotherBoid> motherBoids;
    //private Array<SimpleBoid> simpleBoids;
    //private Behaviour behaviour;
   // private Array<Bullet> basicBullets;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector3 touchPos, diff;
    private boolean wasJustTouched;
    private int len =0, motherIndex, simpleIndex;
    private LevelManager levelManager;
    private final Pool<SimpleBoid> simpleBoidPool = new Pool<SimpleBoid>(){
        @Override
        protected SimpleBoid newObject(){
            return new SimpleBoid(0,0);
        }
    };
    private final Pool<MotherBoid> motherBoidPool = new Pool<MotherBoid>(){
        @Override
        protected MotherBoid newObject(){
            return new MotherBoid(0,0);
        }
    };
    ShapeRenderer shapeRenderer;

    public GameScreen(final MyGdxGame game) {
        levelManager = new LevelManager();
        fpsLogger = new FPSLogger();
        this.game = game;
        /*boids = new Array<Boid>();
        basicBullets = new Array<Bullet>();
        motherBoids = new Array<MotherBoid>();
       simpleBoids = new Array<SimpleBoid>();
        for(int i =0; i<1; i++) {
               MotherBoid boid =motherBoidPool.obtain();
          boid.init(random(MainMenuScreen.SCREEN_WIDTH),random(MainMenuScreen.SCREEN_HEIGHT));
            boids.add(boid);
           motherBoids.add(boid);
        }
        for(int i =0; i<20; i++)
        {
            SimpleBoid boid =simpleBoidPool.obtain();
            boid.init(random(MainMenuScreen.SCREEN_WIDTH),random(MainMenuScreen.SCREEN_HEIGHT));
            boids.add(boid);
            simpleBoids.add(boid);
        }*/
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MainMenuScreen.SCREEN_WIDTH, MainMenuScreen.SCREEN_HEIGHT);
    batch = new SpriteBatch();
    //behaviour = new Behaviour();
    touchPos = new Vector3();
    diff = new Vector3();
    wasJustTouched = false;
    shapeRenderer = new ShapeRenderer();
    intersector = new Intersector();
}

    @Override
    public void show() {
    }




    @Override
    public void render(float delta) {
        //fpsLogger.log();
        levelManager.logic();
        /*len = boids.size;
        simpleIndex=0;
        motherIndex=0;

        for(int i = 0; i<len; i++)
        {
            Boid boid = boids.get(i);
            if(boid.type()=="MotherBoid")
            {
                if(!motherBoids.get(motherIndex).isAlive())
                {

                    motherBoids.removeIndex(motherIndex);

                    motherBoidPool.free(boid.getMother());
                    boids.removeIndex(i);
                    len--;
                }
                else {
                    motherIndex++;
                }
            }
            if(boid.type()=="SimpleBoid")
            {
                if(!simpleBoids.get(simpleIndex).isAlive())
                {
                    simpleBoids.removeIndex(simpleIndex);
                    simpleBoidPool.free(boid.getSimple());
                    boids.removeIndex(i);
                    len--;
                }
                else {
                    simpleIndex++;
                }
            }
        }
        len =basicBullets.size;
        for(int i = 0; i<len; i++)
        {
            if(!basicBullets.get(i).isAlive())
            {
                basicBullets.removeIndex(i);
                len--;
            }
        }
        behaviour.moveBoids(boids);
        for(Boid boid: boids)
        {
            boid.applyRules(boids);
        }*/
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        levelManager.render(batch);
        /*
        for(Bullet bullet: basicBullets)
        {
            bullet.show(batch);
            for(int i = 0; i<boids.size; i++)
            {
                boids.get(i).passPolygon(bullet.getPolygon());
            }
        }
        behaviour.drawBoids(boids, batch);*/
        batch.end();
        debug();


        if(Gdx.input.justTouched())
        {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            wasJustTouched = true;

        }
        if (Gdx.input.isTouched()) {
            diff.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(diff);
            shapeRenderer.setProjectionMatrix(camera.combined);

           /* for(Boid boid: boids)
            {
                boid.follow(new Vector2(diff.x, diff.y));
            }*/

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.line(touchPos.x, touchPos.y, diff.x, diff.y);
            shapeRenderer.end();
            Vector3 buff = new Vector3().set(touchPos.sub(diff));
            touchPos.add(diff);
            diff.set(buff);
            //for(MotherBoid boid: motherBoids)
           // {
                //boid.steer(-diff.x, -diff.y);
           // }

        }
        if(!Gdx.input.isTouched()&&wasJustTouched)
        {
            Vector3 buff = new Vector3().set(touchPos.sub(diff));
            touchPos.add(diff);
            diff.set(buff);
            /*Vector2 bullVel = new Vector2(diff.x, diff.y);
            if (bullVel.len() > 0) {
                    basicBullets.add(new BasicBullet());
                    basicBullets.peek().init(touchPos.x, touchPos.y);
                    basicBullets.peek().addVelocity(diff.x, diff.y);
            }*/





            //System.out.println(diff);
            wasJustTouched=false;
        }
        if(!Gdx.input.isTouched())
        {
            /*for(MotherBoid boid: motherBoids)
            {
                boid.slowDown();
            }*/
        }

    }
    private void debug()
    {
        levelManager.debug(camera);
    }
    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        /*batch.dispose();
        shapeRenderer.dispose();
        for(Boid boid: boids)
        {
            boid.dispose();
        }*/

    }

}
