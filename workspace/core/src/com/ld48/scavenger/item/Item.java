package com.ld48.scavenger.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ld48.scavenger.Player;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.screens.BunkerRoom;
import com.ld48.scavenger.util.GameUtil;

public class Item extends Sprite {
	
	public static enum ItemType {FOOD, AMMO};
	public ItemType type;
	
	private static TextureRegion food_tex = 
			new TextureRegion(Assets.manager.get(Assets.items_small), 0, 0, 16, 16);
	private static TextureRegion ammo_tex = 
			new TextureRegion(Assets.manager.get(Assets.items_small), 16, 0, 16, 16);
	
	private Vector2 velocity = new Vector2();
	private float speed = 60 * 2, gravity = 60 * 1.8f;
	private BunkerRoom room;

	public Item(ItemType type, BunkerRoom room){
		super(new Sprite(food_tex));
		this.type = type;
		
		switch(type){
		case FOOD:
			super.setRegion(food_tex);
			break;
		case AMMO:
			super.setRegion(ammo_tex);
			break;
		}
		
		this.room = room;
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}


	private void update(float delta) {
		float oldy = getY();
		
		//Check for pickup
		if(GameUtil.checkCollisionRect(this, room.getPlayer())){
			pickup(room.getPlayer());
		}
			
		
		//Apply gravity
		velocity.y -= gravity * delta;
		
		//clamp velocity
		if(velocity.y > speed)
			velocity.y = speed;
		else if(velocity.y < -speed)
			velocity.y = -speed;
		
		if(GameUtil.checkCollisionY(this, velocity, room.getCollisionLayer())){
			setY(oldy);
			velocity.y = 0;
		} else {
			setY(getY() + velocity.y * delta);
		}
	}
	
	private void pickup(Player p) {
		switch(type){
		case FOOD:
			p.food++;
			break;
		case AMMO:
			p.ammo++;
			break;
		}
		
		Assets.manager.get(Assets.pickup_wav).play();
		
		room.getItems().remove(this);
	}

	public void spawn(float x, float y, BunkerRoom room){
		setPosition(x, y);
		room.getItems().add(this);
	}
	
	public void setStartPosition(float x, float y) {
		setPosition(x * room.getCollisionLayer().getTileWidth(), (room.getCollisionLayer().getHeight() - (y+1)) * room.getCollisionLayer().getTileHeight());
	}
	
	public BunkerRoom getRoom() {
		return room;
	}

	public void setRoom(BunkerRoom room) {
		this.room = room;
	}
	
}
