package com.ld48.scavenger.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameUtil {
	
	private static String property_boundry = "boundry";
	
	public static boolean checkCollisionX(Sprite collider, Vector2 velocity, TiledMapTileLayer collisionLayer){
		float tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false;
		
		if(velocity.x < 0){
			
			//top left
			collisionX = collisionLayer.getCell(
					(int) (collider.getX() / tileWidth), 
					(int)((collider.getY() + collider.getHeight()) / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//middle left
			if(!collisionX)
				collisionX = collisionLayer.getCell(
						(int) (collider.getX() / tileWidth), 
						(int)((collider.getY() + (collider.getHeight()) / 2) / tileHeight )).getTile().getProperties().containsKey(property_boundry);
			
			//bottom left
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) (collider.getX() / tileWidth), (int)(collider.getY() / tileHeight)+1).getTile().getProperties().containsKey(property_boundry);
			
			
		}else if(velocity.x > 0){
			
			//top right
			collisionX = collisionLayer.getCell(
					(int) ((collider.getX() + collider.getWidth()) / tileWidth),
					(int)((collider.getY() + collider.getHeight()) / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//middle right
			if(!collisionX)
			collisionX = collisionLayer.getCell(
					(int) ((collider.getX() + collider.getWidth()) / tileWidth), 
					(int)((collider.getY() + (collider.getHeight()) / 2) / tileHeight )).getTile().getProperties().containsKey(property_boundry);
			
			//bottom right
			if(!collisionX)
			collisionX = collisionLayer.getCell((int) ((collider.getX() + collider.getWidth()) / tileWidth), (int)(collider.getY() / tileHeight)+1).getTile().getProperties().containsKey(property_boundry);
		}
		return collisionX;
				
	}
	
	public static boolean checkCollisionY(Sprite collider, Vector2 velocity, TiledMapTileLayer collisionLayer){
		float tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionY = false;
		
		
		if(velocity.y < 0){
			//bottom left
			collisionY = collisionLayer.getCell(
					(int) ((collider.getX()+1) / tileWidth), 
					(int)(collider.getY() / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//bottom middle
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) ((collider.getX() + collider.getWidth() / 2) / tileWidth), 
						(int)(collider.getY() / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//bottom right
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) (((collider.getX()-1) + collider.getWidth()) / tileWidth), 
						(int)(collider.getY() / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
		}else if(velocity.y > 0){
			//top left
			collisionY = collisionLayer.getCell(
					(int) ((collider.getX()+1) / tileWidth), 
					(int)((collider.getY() + collider.getHeight()) / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//top middle
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) ((collider.getX() + collider.getWidth() / 2) / tileWidth), 
						(int)((collider.getY() + collider.getHeight()) / tileHeight)).getTile().getProperties().containsKey(property_boundry);
			
			//top right
			if(!collisionY)
				collisionY = collisionLayer.getCell(
						(int) (((collider.getX()-1) + collider.getWidth()) / tileWidth), 
						(int)((collider.getY() + collider.getHeight()) / tileHeight)).getTile().getProperties().containsKey(property_boundry);
		}
		return collisionY;
	}
	
	public static boolean checkCollisionRect(Sprite a, Sprite b){
		Rectangle ra = a.getBoundingRectangle();
		Rectangle rb = b.getBoundingRectangle();
		Rectangle inter = new Rectangle();
		
		return Intersector.intersectRectangles(ra, rb, inter);
	}
	
}
