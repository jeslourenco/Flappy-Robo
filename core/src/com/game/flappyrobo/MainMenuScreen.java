package com.game.flappyrobo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import static com.game.flappyrobo.FlappyRobo.assets;

public class MainMenuScreen implements Screen {

    private final FlappyRobo game;
    private OrthographicCamera camera;
    private Texture bg;
    private Texture changeScreens;
    private Texture button;
    private Sprite msg;
    private float scaleFac = 1f;
    private Vector3 touchPoint;


    public MainMenuScreen(FlappyRobo flappy) {
        this.game = flappy;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FlappyRobo.WIDTH, FlappyRobo.HEIGHT);
        touchPoint = new Vector3();
        bg = assets.loadScreensTexture("bg_menu.png");
        msg = new Sprite(assets.loadScreensTexture("template.png"));
        changeScreens = assets.loadTexture("escolher_tela.png");
        button = assets.loadTexture("button.png");
        msg.setPosition(FlappyRobo.WIDTH / 2f - msg.getWidth() / 2f, FlappyRobo.HEIGHT / 2f - msg.getHeight() / 5f);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (msg.getScaleX() > 1.1f)
            scaleFac = -6f * delta;
        if (msg.getScaleX() < 0.9f)
            scaleFac = 6f * delta;

        msg.scale(scaleFac * delta);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0, FlappyRobo.WIDTH, FlappyRobo.HEIGHT);
        msg.draw(game.batch);
        game.batch.draw(changeScreens, FlappyRobo.WIDTH / 2f - changeScreens.getWidth() / 2f, 70);
        game.batch.draw(button, FlappyRobo.WIDTH - button.getWidth() - 20, 50);
        game.batch.draw(button, 20, 50, button.getWidth(), button.getHeight(), 0, 0, button.getWidth(), button.getHeight(), true, false);
        game.batch.end();

        if (Gdx.input.isTouched())
            input(Gdx.input.getX(), Gdx.input.getY());

    }

    public void input(float x, float y) {
        camera.unproject(touchPoint.set(x, y, 0));
        if (msg.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
            game.setScreen(new GameScreen(game));
            return;
        }
        if (touchPoint.x > FlappyRobo.WIDTH / 2f){
            if (game.screens.next()){
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
        if (touchPoint.x < FlappyRobo.WIDTH / 2f) {
            if (game.screens.prev()) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //game.batch.dispose();
    }
}
