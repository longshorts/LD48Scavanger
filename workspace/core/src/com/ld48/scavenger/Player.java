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
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor{
	
	private Vector2 velocity = new Vector2();
	
	private float maxSpeed = 60 * 2, gravity = 60 * 1.8f;
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
	
	private static TextureRegion tex = new TextureRegion(new Texture("tilesets/player_walkani.png"), 0, 0, 32, 64);

	public Player(Sprite sprite, TiledMapTileLayer collisionLayer){
		super(new Sprite(tex));
		this.collisionLayer = collisionLayer;
		
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
		//Increase by gravity
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

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.W:
			if(canJump){
				velocity.y = maxSpeed;
				canJump = false;
			}
			break;
		case Keys.A:
			velocity.x = -maxSpeed;
			break;
		case Keys.D:
			velocity.x = maxSpeed;
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
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
	
}
