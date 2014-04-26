package com.ld48.scavenger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.screens.BunkerRoom;

public class ScavengerGame extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		Assets.loadAll();
		Assets.manager.finishLoading();
		
		this.setScreen(new BunkerRoom());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose(){
		Assets.disposeAll();
		super.dispose();
	}
}
