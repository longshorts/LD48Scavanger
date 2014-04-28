package com.ld48.scavenger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ld48.scavenger.ScavengerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Scavenger";
		config.width = 640;
		config.height = 400;
		new LwjglApplication(new ScavengerGame(), config);
	}
}
