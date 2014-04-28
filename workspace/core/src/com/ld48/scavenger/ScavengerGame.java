package com.ld48.scavenger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ld48.scavenger.assets.Assets;
import com.ld48.scavenger.item.Item;
import com.ld48.scavenger.npcs.Zombie;
import com.ld48.scavenger.screens.BunkerRoom;
import com.ld48.scavenger.screens.Title;

public class ScavengerGame extends Game {
	public SpriteBatch batch;
	
	public static enum BunkerNum {BUNKER0, BUNKER1};
	
	public BunkerRoom bunker0;
	public TiledMap bunker0_tm;
	public OrthogonalTiledMapRenderer bunker0_r;
	
	public BunkerRoom bunker1;
	public TiledMap bunker1_tm;
	public OrthogonalTiledMapRenderer bunker1_r;
	
	public Player player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		Assets.loadAll();
		Assets.manager.finishLoading();
		
//		bunker0_tm = new TmxMapLoader().load("maps/bunker1.tmx");
		bunker0_tm = new TmxMapLoader().load("maps/fightlevel1.tmx");
		bunker0_r = new OrthogonalTiledMapRenderer(bunker0_tm);
		
		bunker1_tm = new TmxMapLoader().load("maps/bunker1.tmx");
//		bunker1_tm = new TmxMapLoader().load("maps/fightlevel1.tmx");
		bunker1_r = new OrthogonalTiledMapRenderer(bunker1_tm);
		
		player = new Player(new Sprite(new Texture("tilesets/player_walkani.png")));
		
		initialise();
		
		//this.setScreen(new BunkerRoom(this));
		this.setScreen(new Title(this, bunker1));
		Assets.manager.get(Assets.main_wav).loop();
	}
	
	public void initialise(){
		player.revive();
		
		bunker0 = new BunkerRoom(this, BunkerNum.BUNKER0);
		bunker1 = new BunkerRoom(this, BunkerNum.BUNKER1);
		
		player.setRoom(bunker1);
		player.setStartPosition(16, 19);
		Gdx.input.setInputProcessor(player);
		
		//--------------
		//BUNKER0
		//--------------
		Zombie z01 = new Zombie(player, bunker0), 
				z02 = new Zombie(player, bunker0), 
				z03 = new Zombie(player, bunker0);
				
		z01.setStartPosition(16, 12);
		z02.setStartPosition(13, 12);
		z03.setStartPosition(19, 12);
		
		bunker0.getZombies().add(z01);
		bunker0.getZombies().add(z02);
		bunker0.getZombies().add(z03);
		
		Item f1 = new Item(Item.ItemType.AMMO, bunker0);
		f1.setPosition(13 * bunker0.getCollisionLayer().getTileWidth(), (bunker0.getCollisionLayer().getHeight() - 7) * bunker0.getCollisionLayer().getTileHeight());
		bunker0.getItems().add(f1);
		
		//------------
		//BUNKER1
		//------------
		
		Zombie z11 = new Zombie(player, bunker1), 
				z12 = new Zombie(player, bunker1), 
				z13 = new Zombie(player, bunker1), 
				z14 = new Zombie(player, bunker1), 
				z15 = new Zombie(player, bunker1), 
				z16 = new Zombie(player, bunker1), 
				z17 = new Zombie(player, bunker1), 
				z18 = new Zombie(player, bunker1);
		
		z11.setStartPosition(11, 23);
		z12.setStartPosition(16, 23);
		z13.setStartPosition(21, 23);
		z14.setStartPosition(22, 12);
		z15.setStartPosition(41, 12);
		z16.setStartPosition(43, 12);
		z17.setStartPosition(51, 16);
		z18.setStartPosition(53, 16);
		
		bunker1.getZombies().add(z11);
		bunker1.getZombies().add(z12);
		bunker1.getZombies().add(z13);
		bunker1.getZombies().add(z14);
		bunker1.getZombies().add(z15);
		bunker1.getZombies().add(z16);
		bunker1.getZombies().add(z17);
		bunker1.getZombies().add(z18);
		
		Item f11 = new Item(Item.ItemType.AMMO, bunker1);
		f11.setStartPosition(19,19);
		bunker1.getItems().add(f11);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose(){
		Assets.disposeAll();
		super.dispose();
	}
}
