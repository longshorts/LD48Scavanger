package com.ld48.scavenger.npcs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.ld48.scavenger.screens.BunkerRoom;
import com.ld48.scavenger.util.GameUtil;

public class Bullet extends Sprite {
	
	private Vector2 velocity = new Vector2();
	private float speed = 60 * 8;
	private BunkerRoom bunkerRoom;
	private TiledMapTileLayer collisionLayer;
	
	private static final String colProperty = "boundry";
	private static Texture tex = new Texture("tilesets/bullet_image.png");
	
	public Bullet(float x, float y, int direction, TiledMapTileLayer collisionLayer, BunkerRoom screen){
		super(new Sprite(tex));
		bunkerRoom = screen;
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		float oldx = getX(), oldy = getY();
		
		//React to zombie collision
		Zombie[] zombies = bunkerRoom.getZombies();	
		for(int i = 0; i < zombies.length; i++){
			if(GameUtil.checkCollisionRect(this, zombies[i])){
				kill(zombies[i]);
			}
		}
		
		//React to wall collision
		if(GameUtil.checkCollisionX(this, velocity, collisionLayer)){
			setX(oldx);
			velocity.x = 0;
		} else {
			setX(getX() + velocity.x * delta);	//move on x
		}
		
	}

	private void kill(Zombie zombie) {
		zombie.die();
	}
}
