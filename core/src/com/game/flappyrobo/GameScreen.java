package com.game.flappyrobo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.game.flappyrobo.FlappyRobo.assets;


public class GameScreen implements Screen {

    private final FlappyRobo game;
    private OrthographicCamera camera;
    private Robo robo;
    private Points points;
    private Texture bg;
    private Texture ground;
    private Texture gameOver;
    private Texture menu;
    private float TUBO_X = 200;
    private float TUBO_Y = 200;

    private float[] groundsX = new float[3];
    private Array<Rectangle> tubos = new Array<>();
    private Texture tuboTexture;
    private Rectangle bufferRect;
    private Rectangle menuRect;
    private Vector3 touchPoint;


    private GlyphLayout layout;

    public GameScreen(FlappyRobo game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FlappyRobo.WIDTH, FlappyRobo.HEIGHT);

        robo = new Robo();
        points = new Points();
        bg = assets.loadScreensTexture("bg.png");
        ground = assets.loadScreensTexture("ground.png");
        tuboTexture = assets.loadScreensTexture("tubo.png");
        gameOver = assets.loadScreensTexture("gameover.png");
        menu = assets.loadTexture("menu.png");
        menuRect = new Rectangle(10, FlappyRobo.HEIGHT - 10 - menu.getHeight() , menu.getWidth(), menu.getHeight());
        touchPoint = new Vector3();
        layout = new GlyphLayout();

        System.out.println("tubo dist y: " + TUBO_Y);
        for (int i = 0; i < groundsX.length; i++)
            groundsX[i] = i * ground.getWidth();

        Rectangle rectangle = new Rectangle(10, FlappyRobo.HEIGHT + 10 , tuboTexture.getWidth(), tuboTexture.getHeight());

        bufferRect = new Rectangle();
        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));

        for (int i = 0; i < tubos.size; i++) {
            tubos.get(i).x = 500 + TUBO_X * i;
            tubos.get(i).y = -MathUtils.random(100, 250);
        }


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (robo.state == Robo.State.OVER) {
            if (points.getPoints() > game.maxPoints) game.save(points.getPoints());
            if (Gdx.input.justTouched()) reset();
        }

        if (Gdx.input.isTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (menuRect.contains(touchPoint.x, touchPoint.y)){
                game.setScreen(new MainMenuScreen(game));
            }
        }

        robo.input();
        robo.update(delta);


        float mostRight = 0f;


        for (Rectangle tubo : tubos)
            mostRight = Math.max(tubo.x, mostRight);

        for (Rectangle tubo : tubos) {
            if (tubo.x <= -tuboTexture.getWidth()) {
                tubo.x = mostRight + TUBO_X;
                points.setPoints(points.getPoints() + 1);
                robo.point.play();
            }
            if (robo.state == Robo.State.PLAYING)
                tubo.x -= robo.velX * delta;
            bufferRect.set(tubo.x, tubo.y + tubo.height + TUBO_Y, tubo.width, tubo.height);

            if ((tubo.overlaps(robo.rectangle) || bufferRect.overlaps(robo.rectangle)) && robo.state != Robo.State.OVER) {
                robo.state = Robo.State.OVER;
                robo.hit.play();
            }
        }



        mostRight = 0f;
        for (float x : groundsX)
            mostRight = Math.max(x, mostRight);

        for (int i = 0; i < groundsX.length; i++) {
            if (groundsX[i] <= -ground.getWidth())
                groundsX[i] = mostRight + ground.getWidth();
            if (robo.state != Robo.State.OVER)
                groundsX[i] -= robo.velX * delta;
        }


        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0, FlappyRobo.WIDTH, FlappyRobo.HEIGHT);

        for (int i = 0; i < tubos.size; i++) {
            game.batch.draw(tuboTexture, tubos.get(i).x, tubos.get(i).y);
            game.batch.draw(tuboTexture, tubos.get(i).x, tubos.get(i).y + tuboTexture.getHeight() + TUBO_Y,
                    tuboTexture.getWidth(), tuboTexture.getHeight(), 0, 0,
                    tuboTexture.getWidth(), tuboTexture.getHeight(), false, true
            );
        }

        for (float x : groundsX)
            game.batch.draw(ground, x, -70f);

        if (robo.state == Robo.State.OVER) {
            layout.setText(game.font, "Pontos Max: " + game.maxPoints);
            game.batch.draw(gameOver, FlappyRobo.WIDTH / 2f - gameOver.getWidth() / 2f, FlappyRobo.HEIGHT / 2f - gameOver.getHeight() / 2f);
            game.font.draw(game.batch, layout, FlappyRobo.WIDTH / 2f - layout.width / 2, FlappyRobo.HEIGHT / 2f - layout.height / 2f - gameOver.getHeight());
        }

        points.draw(game.batch, FlappyRobo.WIDTH / 2f - points.width / 2f, FlappyRobo.HEIGHT - 100);
        game.batch.draw(menu, menuRect.x, menuRect.y);

        robo.draw(game.batch);
        game.batch.end();
    }


    private void reset() {
        points.setPoints(0);
        tubos.clear();
        robo.reset();
        Rectangle rectangle = new Rectangle(0, 0, tuboTexture.getWidth(), tuboTexture.getHeight());

        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));
        tubos.add(new Rectangle(rectangle));

        for (int i = 0; i < tubos.size; i++) {
            tubos.get(i).x = 500 + TUBO_X * i;
            tubos.get(i).y = -MathUtils.random(100, 250);
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

    }
}
