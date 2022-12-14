package com.game.flappyrobo;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.flappyrobo.FlappyRobo;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(290, 515);
		config.setForegroundFPS(60);
		config.setTitle("FlappyRobo");
		new Lwjgl3Application(new FlappyRobo(), config);
	}
}
