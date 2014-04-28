package com.ld48.scavenger.npcs;

import java.util.ArrayList;

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
	private float speed = 60 * 4;
	private BunkerRoom bunkerRoom;
	private TiledMapTileLayer collisionLayer;
	
	private static Texture tex = new Texture("tilesets/bullet_image.png");
	
	public Bullet(float x, float y, boolean direction, TiledMapTileLayer collisionLayer, BunkerRoom screen){
		super(new Sprite(tex));
		this.collisionLayer = collisionLayer;
		this.bunkerRoom = screen;
		
		this.setPosition(x, y);
		
		if(direction){
			velocity.x = speed;
		} else {
			velocity.x = -speed;
			this.flip(true, false);
		}
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		float oldx = getX();
		
		//React to zombie collision
		ArrayList<Zombie> zombies = bunkerRoom.getZombies();	
		for(int i = 0; i < zombies.size(); i++){
			if(GameUtil.checkCollisionRect(this, zombies.get(i)) && !zombies.get(i).dead){
				kill(zombies.get(i));
				return;
			}
		}
		
		//React to wall collision
		if(GameUtil.checkCollisionX(this, velocity, collisionLayer)){
			setX(oldx);
			bunkerRoom.getBullets().remove(this);
		} else {
			setX(getX() + velocity.x * delta);	//move on x
		}
		
	}

	private void kill(Zombie zombie) {
		zombie.die();
		
		if(!bunkerRoom.getBullets().remove(this)){
			System.out.println("Bullet Not removed!");
		}
	}
}
