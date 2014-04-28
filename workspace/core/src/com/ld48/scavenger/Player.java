package com.ld48.scavenger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.npcs.Bullet;
import com.ld48.scavenger.screens.BunkerRoom;

public class Player extends Sprite implements InputProcessor{
	
	//Game variables
	public int food = 5;
	public int ammo = 5;
	public float hunger = 1;
	public boolean dead = false;
	private float hungerRate = 0.05f;
	
	private Vector2 velocity = new Vector2();
	
	private float maxSpeed = 60 * 2, gravity = 60 * 8.0f, jumpSpeed = 60 * 5;
	private TiledMapTileLayer collisionLayer;
	private boolean canJump = true;
	private static final String colProperty = "boundry";
	
	//Animation stuff
	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 2;
	
	Animation walkAnimation;
	Animation jumpAnimation;
	Texture animationSheet;
	TextureRegion[] walkFrames;
	TextureRegion[] jumpFrames;
	TextureRegion currentFrame;
	
	float walkStateTime, jumpStateTime;

	private BunkerRoom room;
	
	private static TextureRegion tex = new TextureRegion(new Texture("tilesets/player_walkani.png"), 0, 0, 32, 64);

	public Player(Sprite sprite){
		super(new Sprite(tex));
		
		animationSheet = new Texture("tilesets/player_tiles.png");
		TextureRegion[][] tmp = TextureRegion.split(animationSheet, animationSheet.getWidth()/FRAME_COLS, animationSheet.getHeight()/FRAME_ROWS);
		
		walkFrames = new TextureRegion[FRAME_COLS];
		jumpFrames = new TextureRegion[2];
		
		walkFrames[0] = tmp[0][0];
		walkFrames[1] = tmp[0][1];
		walkFrames[2] = tmp[0][2];
		walkFrames[3] = tmp[0][3];
		jumpFrames[0] = tmp[1][0];
		jumpFrames[1] = tmp[1][1];
		
		walkAnimation = new Animation(0.1f, walkFrames);
		walkStateTime = 0f;
		jumpAnimation = new Animation(0.1f, jumpFrames);
		jumpStateTime = 0f;
	}
	
	@Override
	public void draw(Batch batch){
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		//Process hunger
		if(!dead){
			hunger -= hungerRate * delta;
			if(hunger <= 0){
				food--;
				if(food <= 0){
					die();
				}
				if(!dead)hunger = 1;
			}
		}
		
		
		//Increase by gravity
		velocity.y -= gravity * delta;
		
		//clamp velocity
		if(velocity.y > jumpSpeed)
			velocity.y = jumpSpeed;
		else if(velocity.y < -jumpSpeed)
			velocity.y = -jumpSpeed;
				
		float oldx = getX(), oldy = getY();
		
		//React to collision
		if(checkCollisionX()){
			setX(oldx);
			velocity.x = 0;
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
		//Jumping
		if(velocity.y > 0){
			if(this.isFlipX()){
				this.setRegion(jumpFrames[0]);
				this.setFlip(true, false);
			} else {
				this.setRegion(jumpFrames[0]);
			}
			
		//Falling
		} else if(velocity.y < 0){
			if(this.isFlipX()){
				this.setRegion(jumpFrames[1]);
				this.setFlip(true, false);
			} else {
				this.setRegion(jumpFrames[1]);
			}
		}
		else if(velocity.x != 0){
			walkStateTime += delta;
			currentFrame = walkAnimation.getKeyFrame(walkStateTime, true);
			this.setRegion(currentFrame);
		} 
		if(velocity.x < 0) this.setFlip(true, false);
		
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
			
			canJump = collisionY;
			if(canJump){
				if(this.isFlipX()){
					this.setRegion(walkFrames[0]);
					this.setFlip(true, false);
				} else {
					this.setRegion(walkFrames[0]);
				}
				
			}
			
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

	private void shoot(){
		//Check if there is still ammo
		if(ammo <= 0){
			return;
		} else {
			ammo--;
		}
		
		float x = 0;
		if(this.isFlipX()){
			x = getX();
		} else {
			x = getX() + getWidth();
		}
		float y = getY() + (getHeight() / 2) + 4;
		Bullet b = new Bullet(x, y, !isFlipX(), collisionLayer, room);
		room.getBullets().add(b);
		
		Assets.manager.get(Assets.gun_wav).play();
	}
	
	public void die(){
		if(!dead){
			Assets.manager.get(Assets.death_wav).play();
			velocity.x = 0;
		}
		dead = true;
	}
	
	public void setStartPosition(float x, float y) {
		setPosition(x * getCollisionLayer().getTileWidth(), (getCollisionLayer().getHeight() - (y+1)) * getCollisionLayer().getTileHeight());
	}
	
	public void revive(){
		if(dead){
			food = 5;
			ammo = 5;
			hunger = 1;
			dead = false;
		}
	}
	
	private void interact(){
		System.out.println("x" + getX() + "y" + getY());
		TiledMapTileLayer objects = room.getObjectLayer();
		TiledMapTileLayer background = room.getBackgroundLayer();
		float tileWidth = objects.getTileWidth(), tileHeight = objects.getTileHeight();
		boolean loot = false;
		Cell c;
		Cell back_c;
		
		try{
			c = objects.getCell(
					(int) ((getX() + (getWidth() / 2)) / tileWidth), 
					(int)((getY() + (getHeight()/4)) / tileHeight));
			back_c = background.getCell(
					(int) ((getX() + (getWidth() / 2)) / tileWidth), 
					(int)((getY() + (getHeight()/4)) / tileHeight));
			loot = c.getTile().getProperties().containsKey("loot");
		} catch(NullPointerException e){
			loot = false;
			return;
		}
		
		if(loot){
			Assets.manager.get(Assets.pickup_wav).play();
			food += 2;
			ammo += 2;
			c.setTile(back_c.getTile());
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(dead){
			return false;
		}
		switch(keycode){
		case Keys.W:
			if(canJump){
				velocity.y = jumpSpeed;
				canJump = false;
				Assets.manager.get(Assets.jump_wav).play();
			}
			break;
		case Keys.A:
			velocity.x = -maxSpeed;
			break;
		case Keys.D:
			velocity.x = maxSpeed;
			break;
		case Keys.SPACE:
			shoot();
			break;
		case Keys.E:
			interact();
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(dead){
			return false;
		}
		switch(keycode){
		case Keys.A:
		case Keys.D:
			velocity.x = 0;
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public boolean isCanJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public static TextureRegion getTex() {
		return tex;
	}

	public static void setTex(TextureRegion tex) {
		Player.tex = tex;
	}

	public BunkerRoom getRoom() {
		return room;
	}

	public void setRoom(BunkerRoom room) {
		this.room = room;
		this.collisionLayer = room.getCollisionLayer();
	}
	
}
