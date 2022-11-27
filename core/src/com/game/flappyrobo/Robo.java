package com.game.flappyrobo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Robo {
    public float x = 0, y = 0;
    public float rotation = 0f;
    public float velY = 0, velX = 0f;
    public Rectangle rectangle;
    public float G = 9.807f * 2f;
    public int width, height;
    public State state;

    private static final float RECHARGE = 0.4f;
    private float recharge = RECHARGE;
    private Array<TextureRegion> textures = new Array<>();
    private int frame = 0;
    private float lastFrameTime = 0;
    private float funTime = 0;

    public Sound hit;
    public Sound robot;
    public Sound point;


    public enum State {
        OVER, FUN, PLAYING
    }


    public Robo() {
        textures.add(new TextureRegion(FlappyRobo.assets.loadScreensTexture("robo_1.png")));
        textures.add(new TextureRegion(FlappyRobo.assets.loadScreensTexture("robo_2.png")));
        textures.add(new TextureRegion(FlappyRobo.assets.loadScreensTexture("robo_3.png")));
        hit = FlappyRobo.assets.loadSound("hit.ogg");
        robot = FlappyRobo.assets.loadSound("robot.ogg");
        point = FlappyRobo.assets.loadSound("point.ogg");

        width = textures.get(frame).getRegionWidth();
        height = textures.get(frame).getRegionHeight();
        rectangle = new Rectangle(x, y, width, height);
        x = 20;
        y = FlappyRobo.HEIGHT / 2f;
        state = State.FUN;
        robot.play();
    }


    public void update(float delta) {
        recharge -= delta;
        lastFrameTime -= delta;
        if (lastFrameTime < 0 && state != State.OVER) {
            lastFrameTime = 1f / 12f;
            frame = (frame < 2) ? frame + 1 : 0;
        }

        if (state == State.PLAYING){
            velX = 80f;
            G = 9.807f * 2f;
        }

        if (state == State.FUN) {
            funTime -= delta;
            G = 2f;
            if (recharge < 0 && velY < 0) {
                recharge = RECHARGE;
                velY= 0;
                velY += 0.5f;
                velX = 80f;
            }
        }

        if (state != State.OVER && y < 20){
            state = State.OVER;
            velX = 0f;

            robot.stop();
            hit.play();
        }

        velY -= delta * G;
        rectangle.setPosition(x, y);

        rotation = rotation * -delta * 3;
        for (int i = 0; i < 9; i++)
            if (velY < -i * 1.5f) rotation = (i * -10f);
        y = MathUtils.clamp(y + velY, 0, FlappyRobo.HEIGHT);

    }

    public void input() {
        if (state == State.OVER){
            robot.stop();

            return;
        }

        if (state == State.FUN && Gdx.input.justTouched() && funTime < 0){
            G = 9.807f * 2f;
            state = State.PLAYING;
            return;
        }

        if (state == State.PLAYING && Gdx.input.justTouched() && recharge < 0) {
            velY = 0;
            velY += 8f;
            recharge = RECHARGE;
            rotation = 25f;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textures.get(frame), x, y, width / 2f, height / 2f, width, height, 1f, 1f, rotation);
    }


    public void reset(){
        x = 20;
        y = FlappyRobo.HEIGHT / 2f;
        state = State.FUN;
        funTime = 1f;

        hit.stop();
        robot.play();
    }

}
