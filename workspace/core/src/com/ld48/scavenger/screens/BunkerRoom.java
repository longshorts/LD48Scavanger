package com.ld48.scavenger.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ld48.scavenger.Player;
import com.ld48.scavenger.ScavengerGame;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.item.Item;
import com.ld48.scavenger.npcs.Bullet;
import com.ld48.scavenger.npcs.Zombie;
import com.ld48.scavenger.util.Hud;

public class BunkerRoom implements Screen {
	
	public final ScavengerGame game;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private BitmapFont font = Assets.manager.get(Assets.font);
	
	private Player player;
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer objectLayer;
	private TiledMapTileLayer backgroundLayer;
	private Hud hud;
	
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Item> items = new ArrayList<Item>();

	public BunkerRoom(ScavengerGame game, ScavengerGame.BunkerNum bn){
		this.game = game;
		
		switch(bn){
		case BUNKER0:
			map = game.bunker0_tm;
			renderer = game.bunker0_r;
			break;
		case BUNKER1:
			map = game.bunker1_tm;
			renderer = game.bunker1_r;
		}
		camera = new OrthographicCamera();
		player = game.player;
		
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("forground");
		setObjectLayer((TiledMapTileLayer) map.getLayers().get("objects"));
		setBackgroundLayer((TiledMapTileLayer) map.getLayers().get("background"));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && player.dead){
			game.initialise();
			game.setScreen(new Title(game, game.bunker1));
			this.dispose();
		}
		
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		camera.update();

		renderer.setView(camera);
		renderer.render();
		
		//Render Objects
		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
		
		for(int i = 0; i < zombies.size(); i++){
			zombies.get(i).draw(renderer.getSpriteBatch());
		}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(renderer.getSpriteBatch());
		}
		for(int i = 0; i < items.size(); i++){
			items.get(i).draw(renderer.getSpriteBatch());
		}
		
		//Draw UI
		hud.draw(renderer.getSpriteBatch());
		
		renderer.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;

	}

	@Override
	public void show() {
		
		
		/*player.setRoom(this);
		player.setCollisionLayer(collisionLayer);
		player.setPosition(16 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 9) * player.getCollisionLayer().getTileHeight());
		*/
		hud = new Hud(this, player);
		
		/*Zombie z1 = new Zombie(player, this), 
		z2 = new Zombie(player, this), 
		z3 = new Zombie(player, this);
		
		//z1.setPosition(16 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		//z2.setPosition(13 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		//z3.setPosition(19 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		
		zombies.add(z1);
		zombies.add(z2);
		zombies.add(z3);*/
		
		
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
		hud.dispose();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Zombie> getZombies() {
		return zombies;
	}

	public void setZombies(ArrayList<Zombie> zombies) {
		this.zombies = zombies;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public TiledMapTileLayer getObjectLayer() {
		return objectLayer;
	}

	public void setObjectLayer(TiledMapTileLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public TiledMapTileLayer getBackgroundLayer() {
		return backgroundLayer;
	}

	public void setBackgroundLayer(TiledMapTileLayer backgroundLayer) {
		this.backgroundLayer = backgroundLayer;
	}

}
