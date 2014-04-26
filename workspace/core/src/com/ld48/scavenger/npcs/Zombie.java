package com.ld48.scavenger.npcs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.ld48.scavenger.Player;

public class Zombie extends Sprite{
	
	private Vector2 velocity = new Vector2();
	private boolean dead = false;
	
	private float maxSpeed = 60 * 2, gravity = 60 * 1.8f;
	private TiledMapTileLayer collisionLayer;
	private Player player;
	private static final String colProperty = "boundry";
	
	private static TextureRegion passive_tex = new TextureRegion(new Texture("tilesets/zombie_tiles.png"), 0, 0, 32, 64);
	private static TextureRegion alert_tex = new TextureRegion(new Texture("tilesets/zombie_tiles.png"), 64, 0, 32, 64);
	
	public Zombie(Player player, TiledMapTileLayer collisionLayer){
		super(new Sprite(passive_tex));
		this.collisionLayer = collisionLayer;
		this.player = player;
		velocity.x = maxSpeed / 2;
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		
		//Begin AI
		if(player.getY()+(player.getHeight()/2) < getY()+getHeight() &&
				player.getY()+(player.getHeight()/2) > getY()){
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
		dead = true;
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
}
