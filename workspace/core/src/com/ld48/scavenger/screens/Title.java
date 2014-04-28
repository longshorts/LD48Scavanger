package com.ld48.scavenger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ld48.scavenger.ScavengerGame;
import com.ld48.scavenger.assets.Assets;

public class Title implements Screen{
	
	private final ScavengerGame game;
	private OrthographicCamera camera;
	private Sprite titleBackground;
	
	private BunkerRoom startRoom;
	
	private float fadeRate = 1f;
	
	private BitmapFont font = Assets.manager.get(Assets.font);
	private Skin labelSkin;
	
	private Label label_start;
	
	private boolean textFadeIn = true;
	private boolean startGame = false;
	
	public Title(final ScavengerGame game, BunkerRoom startRoom){
		this.game = game;
		this.startRoom = startRoom;
		camera = new OrthographicCamera();
		
		titleBackground = new Sprite(Assets.manager.get(Assets.titleimg));
		
		labelSkin = new Skin();
		labelSkin.add("default", new LabelStyle(font, Color.WHITE));
		label_start = new Label("Press E to begin", labelSkin);
		label_start.setPosition(210, 150);
		label_start.setColor(255,255,255,0);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		//Fade in
		if(titleBackground.getColor().a < 1){
			if((titleBackground.getColor().a + (fadeRate * delta)) >= 1){
				titleBackground.setAlpha(1);
				fadeStartText(delta);
			} else {
				titleBackground.setAlpha(titleBackground.getColor().a + (fadeRate * delta));
			}
		}
		
		game.batch.begin();
		titleBackground.draw(game.batch);
		label_start.draw(game.batch, label_start.getColor().a);
		game.batch.end();
		
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			startGame = true;
			label_start.setColor(255, 255, 255, 0);
		}
		
		if(startGame){
			if((titleBackground.getColor().a - (fadeRate * delta)) <= 0){
				game.player.revive();
				game.setScreen(startRoom);
				this.dispose();
			} else {
				titleBackground.setAlpha(titleBackground.getColor().a - (fadeRate * delta));
			}
		}
	}

	private void fadeStartText(float delta){
		
		if(startGame) return;
		
		if(textFadeIn){
			if(label_start.getColor().a + (fadeRate * delta) >= 1){
				label_start.setColor(255, 255, 255, 1);
				textFadeIn = false;
			} else {
				label_start.setColor(255,255,255,label_start.getColor().a + (fadeRate * delta));
			}
		} else {
			if(label_start.getColor().a - (fadeRate * delta) <= 0){
				label_start.setColor(255, 255, 255, 0);
				textFadeIn = true;
			} else {
				label_start.setColor(255,255,255,label_start.getColor().a - (fadeRate * delta));
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		titleBackground.setAlpha(0);
	}

	@Override
	public void hide() {
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
	public void dispose() {
		labelSkin.dispose();
	}

}
