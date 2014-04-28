package com.ld48.scavenger.npcs;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.ld48.scavenger.Player;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.item.Item;
import com.ld48.scavenger.screens.BunkerRoom;
import com.ld48.scavenger.util.GameUtil;

public class Zombie extends Sprite{
	
	private Vector2 velocity = new Vector2();
	public boolean dead = false;
	private static final float deathTimer = 5f;
	private float currentDeathTimer = deathTimer;
	
	private BunkerRoom bunkerRoom;
	
	private float maxSpeed = 60 * 2, gravity = 60 * 1.8f;
	private TiledMapTileLayer collisionLayer;
	private Player player;
	private static final String colProperty = "boundry";
	
	private static TextureRegion passive_tex = new TextureRegion(new Texture("tilesets/zombie_tiles.png"), 0, 0, 32, 64);
	private static TextureRegion alert_tex = new TextureRegion(new Texture("tilesets/zombie_tiles.png"), 64, 0, 32, 64);
	
	public Zombie(Player player, BunkerRoom bunkerRoom){
		super(new Sprite(passive_tex));
		this.collisionLayer = bunkerRoom.getCollisionLayer();
		this.player = player;
		this.bunkerRoom = bunkerRoom;
		velocity.x = maxSpeed / 2;
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		
		//Check if dead
		if(dead){
			this.setAlpha(this.getColor().a * (currentDeathTimer / deathTimer));
			currentDeathTimer -= delta;
			if(currentDeathTimer <= 0){
				//passive_tex.getTexture().dispose();
				//alert_tex.getTexture().dispose();
				bunkerRoom.getZombies().remove(this);
				return;
			}
			return;
		}
		
		//Check if touching player
		if(GameUtil.checkCollisionRect(this, player)){
			player.die();
		}
		
		//Begin AI
		if(player.getY()+(player.getHeight()/2) < getY()+getHeight() &&
				player.getY()+(player.getHeight()/2) > getY()
				&& !player.dead){
			pursue();
		} else {
			wander();
		}
		
		//Gravity
		velocity.y -= gravity * delta;
				
		//clamp velocity
		if(velocity.y > maxSpeed)
			velocity.y = maxSpeed;
		else if(velocity.y < -maxSpeed)
			velocity.y = -maxSpeed;
		
		float oldx = getX(), oldy = getY();
		
		//React to collision
		if(checkCollisionX()){
			setX(oldx);
			velocity.x = -velocity.x;
		} else {
			setX(getX() + velocity.x * delta);	//move on x
		}
		
		if(checkCollisionY()){
			setY(oldy);
			velocity.y = 0;
		} else {
			setY(getY() + velocity.y * delta);	//move on y
		}
		
		//Animation
		if(velocity.x > 0){
			this.setFlip(false, false);
		} else if(velocity.x < 0){
			this.setFlip(true, false);
		}
			
		
	}

	private void wander() {
		Random rand = new Random();
		int randNum = rand.nextInt((1000 - 1) + 1) + 1;
		
		//Randomise movement
		if(randNum <= 5) velocity.x = -velocity.x;
		
		if(velocity.x > 0) velocity.x = maxSpeed / 2;
		else if(velocity.x < 0) velocity.x = -(maxSpeed / 2);
		else {
			if(!this.isFlipX()) velocity.x = maxSpeed / 2;
			else velocity.x = -(maxSpeed / 2);
		}
		this.setRegion(passive_tex);
	}

	private void pursue() {
		this.setRegion(alert_tex);
		if(player.getX() > getX()){
			velocity.x = maxSpeed;
		} else if(player.getX() < getX()){
			velocity.x = -maxSpeed;
		}
	}
	
	public void die(){
		if(!dead){
			//Spawn loot
			Item item = new Item(Item.ItemType.FOOD, bunkerRoom);
			item.setTexture(new Texture("tilesets/items_large_tiles.png"));
			item.spawn(getX()+(getWidth()/2), getY()+(getHeight()/2), bunkerRoom);
		
			Assets.manager.get(Assets.hit_wav).play();
		}
		
		dead = true;
	}
	
	public void setStartPosition(float x, float y) {
		setPosition(x * getCollisionLayer().getTileWidth(), (getCollisionLayer().getHeight() - (y+1)) * getCollisionLayer().getTileHeight());
	}
	
	/**Check for collision on X axis**/
	private boolean checkCollisionX(){
		float tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false;
		
		if(velocity.x < 0){
			this.setFlip(true, false);
			
			//top left
			collisionX = collisionLayer.getCell(
					(int) (getX() / tileWidth), 
					(int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//middle left
			if(!collisionX)
				collisionX = collisionLayer.getCell(
						(int) (getX() / tileWidth), 
						(int)((getY() + (getHeight()) / 2) / tileHeight )).getTile().getProperties().containsKey(colProperty);
			
			//bottom left
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int)(getY() / tileHeight)+1).getTile().getProperties().containsKey(colProperty);
			
			
		}else if(velocity.x > 0){
			this.setFlip(false, false);
			
			//top right
			collisionX = collisionLayer.getCell(
					(int) ((getX() + getWidth()) / tileWidth),
					(int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//middle right
			if(!collisionX)
			collisionX = collisionLayer.getCell(
					(int) ((getX() + getWidth()) / tileWidth), 
					(int)((getY() + (getHeight()) / 2) / tileHeight )).getTile().getProperties().containsKey(colProperty);
			
			//bottom right
			if(!collisionX)
			collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int)(getY() / tileHeight)+1).getTile().getProperties().containsKey(colProperty);
		}
		return collisionX;
		
	}
	
	/**Check for collision on Y axis**/
	private boolean checkCollisionY(){
		float tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionY = false;
		
		//System.out.println("Sprite X: " + getX() + )
		
		if(velocity.y < 0){
			//bottom left
			collisionY = collisionLayer.getCell(
					(int) ((getX()+1) / tileWidth), 
					(int)(getY() / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//bottom middle
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) ((getX() + getWidth() / 2) / tileWidth), 
						(int)(getY() / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//bottom right
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) (((getX()-1) + getWidth()) / tileWidth), 
						(int)(getY() / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
		}else if(velocity.y > 0){
			//top left
			collisionY = collisionLayer.getCell(
					(int) ((getX()+1) / tileWidth), 
					(int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//top middle
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) ((getX() + getWidth() / 2) / tileWidth), 
						(int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(colProperty);
			
			//top right
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) (((getX()-1) + getWidth()) / tileWidth), 
						(int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(colProperty);
		}
		return collisionY;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}
}
