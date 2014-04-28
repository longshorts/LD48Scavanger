package com.ld48.scavenger.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ld48.scavenger.Player;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.screens.BunkerRoom;

public class Hud extends Sprite{
	
	private BunkerRoom room;
	private OrthographicCamera camera;
	private Player player;
	private BitmapFont font = Assets.manager.get(Assets.font);
	private Skin labelSkin;
	
	private Sprite hungerBar;
	private float hungerBarBaseWidth;
	private Sprite gameOver;

	Label label_food;
	Label label_ammo;
	Label thanks;
	
	private float basex;
	private float basey;
	
	public Hud(BunkerRoom room, Player player){
		super(new Sprite(Assets.manager.get(Assets.ui)));
		this.room = room;
		this.camera = room.getCamera();
		this.player = player;
		
		hungerBar = new Sprite(Assets.manager.get(Assets.hungerbarbase));
		hungerBarBaseWidth = hungerBar.getWidth();
		gameOver = new Sprite(Assets.manager.get(Assets.gameoverimg));
		
		labelSkin = new Skin();
		labelSkin.add("default", new LabelStyle(font, Color.WHITE));
		label_food = new Label("0", labelSkin);
		label_ammo = new Label("0", labelSkin);
		thanks = new Label("That's it, thanks for playing!", labelSkin);
		
		//label_food = new Label()
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
		drawComponents(batch);
	}

	private void update(float deltaTime) {
		//Get base position from camera
		basex = camera.position.x - (camera.viewportWidth/2);
		basey = camera.position.y + (camera.viewportHeight/2);
		
		//Adjustment
		basex += 5;
		basey -= this.getHeight() + 5;
		
		//Position hud background
		this.setPosition(basex, basey);
	}
	
	private void drawComponents(Batch batch){
		
		label_food.setPosition(basex+50, basey+37);
		//label_food.setPosition(basex-(label_food.getWidth()/2)+50, basey+37);
		label_food.setText("" + player.food);
		label_food.draw(batch, this.getColor().a);
		
		
		label_ammo.setPosition(basex+120, basey+37);
		//label_ammo.setPosition(basex-(label_food.getWidth()/2)+130, basey+37);
		label_ammo.setText("" + player.ammo);
		label_ammo.draw(batch, this.getColor().a);
		
		hungerBar.setPosition(basex+99, basey+10);
		//hungerBar.setColor(255*player.hunger, 255*(1-player.hunger), 255, this.getColor().a);
		hungerBar.setColor(Color.GREEN);
		hungerBar.setBounds(hungerBar.getX(), hungerBar.getY(), hungerBarBaseWidth*player.hunger, hungerBar.getHeight());
		hungerBar.draw(batch);
		
		thanks.setPosition(2400, 220);
		thanks.draw(batch, this.getColor().a);
		
		if(room.getPlayer().dead){
			gameOver.setPosition(basex, basey-gameOver.getHeight()+70);
			gameOver.draw(batch);
		}
	}
	
	public void dispose() {
		labelSkin.dispose();
	}
	
}
