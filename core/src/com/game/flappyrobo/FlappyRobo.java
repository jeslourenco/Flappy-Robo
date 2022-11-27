package com.game.flappyrobo;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class FlappyRobo extends Game {
	public SpriteBatch batch;
	public ShapeRenderer debug;
	public BitmapFont font;
	public Audio audio;
	public int maxPoints = 0;
	public Screens screens;
	public static Assets assets;
	public static int WIDTH = 320, HEIGHT = 560;
	private Preferences preferences;


	@Override
	public void create () {
		batch = new SpriteBatch();
		debug = new ShapeRenderer();
		audio = Gdx.audio;
		debug.setAutoShapeType(true);
		font = new BitmapFont(Gdx.files.internal("pixel.fnt"));
		screens = new Screens();
		assets = new Assets(screens.current());
		preferences = Gdx.app.getPreferences("points");
		maxPoints = preferences.getInteger("maxPoints");
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Gdx.app.log("Flappy: ", "Disposing");
		batch.dispose();
		font.dispose();
		debug.dispose();
		assets.dispose();
	}

	public void save(int points){
		maxPoints = points;
		preferences.putInteger("maxPoints", points);
		preferences.flush();

	}
}
