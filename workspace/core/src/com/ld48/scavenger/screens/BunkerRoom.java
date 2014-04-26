package com.ld48.scavenger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ld48.scavenger.Player;
import com.ld48.scavenger.npcs.Zombie;

public class BunkerRoom implements Screen {
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private Player player;
	
	private Zombie[] zombies;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		camera.update();

		renderer.setView(camera);
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
		for(int i = 0; i < zombies.length; i++){
			zombies[i].draw(renderer.getSpriteBatch());
		}
		renderer.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;

	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/fightlevel1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("forground");
		
		player = new Player(new Sprite(new Texture("tilesets/player_walkani.png")), collisionLayer);
		player.setPosition(16 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 9) * player.getCollisionLayer().getTileHeight());
		
		zombies = new Zombie[3];
		zombies[0] = new Zombie(player, collisionLayer);
		zombies[0].setPosition(16 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		zombies[1] = new Zombie(player, collisionLayer);
		zombies[1].setPosition(13 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		zombies[2] = new Zombie(player, collisionLayer);
		zombies[2].setPosition(19 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 13) * player.getCollisionLayer().getTileHeight());
		
		Gdx.input.setInputProcessor(player);
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
		map.dispose();
		renderer.dispose();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Zombie[] getZombies() {
		return zombies;
	}

	public void setZombies(Zombie[] zombies) {
		this.zombies = zombies;
	}

}
