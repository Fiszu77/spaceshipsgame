package com.drop.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.drop.game.MainMenuScreen.scl;


/**
 * Created by fiszu on 07.08.2017.
 */

public class Behaviour{

    private Vector2 rule1, rule2, rule3, mass, centerOfMass, antiCollide, sumOfVel, diff;
    private Boid temp;
    private int count;
    private float maxSpeed, cohR, followR, alR, sepR, followSepBuff;

    public void setMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}

    public void setCohR(float cohR) {
        this.cohR = cohR;
    }

    public void setFollowR(float followR) {
        this.followR = followR;
    }

    public void setAlR(float alR) {
        this.alR = alR;
    }

    public void setSepR(float sepR) {
        this.sepR = sepR;
    }

    Behaviour() {
        rule1 = new Vector2();
        rule2 = new Vector2();
        rule3 = new Vector2();
        centerOfMass = new Vector2();
        antiCollide = new Vector2();
        sumOfVel = new Vector2();
        diff = new Vector2();
        mass = new Vector2();
        count = 0;
        maxSpeed = 10.0f*scl;
        cohR = 300.0f*scl;
        alR = 300.0f*scl;
        sepR = 130.0f*scl;
        followR = 150.0f*scl;
        followSepBuff = 1.0f*scl;
    }

    Vector2 follow(Vector2 desired, Boid theBoid)
    {
        float d = desired.dst(theBoid.getLocationVector());

            desired.set(desired.sub(theBoid.getLocationVector()));
            desired.nor();
            desired.scl(maxSpeed);
            desired.sub(theBoid.getVelocityVector());
            desired.limit(0.001f*d);
            desired.limit(0.1f);

            return desired;

    }

    Vector2 cohesion(Array<Boid> boids, Boid theBoid) {
        mass.set(0,0);
        count = 0;
        for(int i = 0; i<boids.size; i++)
        {
                if(!boids.get(i).equals(theBoid))
                {
                    if((boids.get(i).getLocationVector().dst(theBoid.getLocationVector()))<cohR)
                    {

                        mass.add(boids.get(i).getLocationVector());
                        count++;

                    }
                }
        }
        if(count>0)
        {
            mass.scl(1.0f/(float)count);
            Vector2 desired = new Vector2().set(mass.sub(theBoid.getLocationVector()));
            desired.nor();
            desired.scl(maxSpeed);
            desired.sub(theBoid.getVelocityVector());
            desired.limit(0.05f);
            return desired;
        }
        else
            return new Vector2();
    }
    Vector2 alignment(Array<Boid> boids,Boid theBoid)
    {

        count = 0;
        sumOfVel.set(0,0);
        for(int i = 0; i<boids.size; i++)
        {
                if(!boids.get(i).equals(theBoid))
                {
                    if((theBoid.getLocationVector().dst(boids.get(i).getLocationVector()))<alR)
                    {
                        count++;
                        sumOfVel.add(boids.get(i).getVelocityVector());
                    }
                }
        }
        if(count>0) {
            sumOfVel.scl(1.0f / (float)count);
            sumOfVel.nor();
            sumOfVel.scl(maxSpeed);
            sumOfVel.sub(theBoid.getVelocityVector());
            sumOfVel.limit(0.05f);
            return sumOfVel;
            //System.out.println(sumOfVel);
        }
        else
            return new Vector2();
    }
    Vector2 separation(Array<Boid> boids, Boid theBoid)
    {
        count=0;
        antiCollide.set(0,0);
        for(int i = 0; i<boids.size; i++)
        {
             if(!boids.get(i).equals(theBoid))
                {
                    float d =boids.get(i).getLocationVector().dst(theBoid.getLocationVector());
                    if(d<sepR)
                    {
                        count++;
                        //System.out.println(boid.getLocationVector().dst(boids.get(i).getLocationVector()));
                        diff.set(theBoid.getLocationVector().sub(boids.get(i).getLocationVector()));
                        diff.nor();
                        diff.scl(1.0f/d);
                        theBoid.getLocationVector().add(boids.get(i).getLocationVector());
                        antiCollide.add(diff);
                    }
                }
        }
        if(count>0)
        {
            antiCollide.scl(1.0f/(float)count);

        }
        if(antiCollide.len()>0)
        {
            antiCollide.nor();
            antiCollide.scl(maxSpeed);
            antiCollide.sub(theBoid.getVelocityVector());
            antiCollide.limit(0.08f);
            return antiCollide;
        }
        else
            return new Vector2();
    }

    void moveBoids(Array<Boid> boids) {
        for(Boid boid: boids)
        {
            boid.move();
        }
    }

    void drawBoids(Array<Boid> boids, Batch batch) {
        for(Boid boid: boids)
        {
            boid.show(batch);
        }
    }
}
