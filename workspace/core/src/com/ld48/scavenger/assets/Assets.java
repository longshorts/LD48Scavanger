package com.ld48.scavenger.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	//Images
	public static final AssetDescriptor<BitmapFont> font =
			new AssetDescriptor<BitmapFont>("fonts/NarkismWhite.fnt", BitmapFont.class);
	public static final AssetDescriptor<Texture> items_small =
			new AssetDescriptor<Texture>("tilesets/items_small_tiles.png", Texture.class);
	public static final AssetDescriptor<Texture> items_large =
			new AssetDescriptor<Texture>("tilesets/items_large_tiles.png", Texture.class);
	public static final AssetDescriptor<Texture> ui =
			new AssetDescriptor<Texture>("img/ui.png", Texture.class);
	public static final AssetDescriptor<Texture> hungerbarbase =
			new AssetDescriptor<Texture>("img/hungerbarbase.png", Texture.class);
	public static final AssetDescriptor<Texture> titleimg =
			new AssetDescriptor<Texture>("img/title.png", Texture.class);
	public static final AssetDescriptor<Texture> gameoverimg =
			new AssetDescriptor<Texture>("img/gameover.png", Texture.class);
	
	//Sounds
	public static final AssetDescriptor<Sound> death_wav =
			new AssetDescriptor<Sound>("sounds/death.wav", Sound.class);
	public static final AssetDescriptor<Sound> gun_wav =
			new AssetDescriptor<Sound>("sounds/gun.wav", Sound.class);
	public static final AssetDescriptor<Sound> hit_wav =
			new AssetDescriptor<Sound>("sounds/hit.wav", Sound.class);
	public static final AssetDescriptor<Sound> jump_wav =
			new AssetDescriptor<Sound>("sounds/jump.wav", Sound.class);
	public static final AssetDescriptor<Sound> pickup_wav =
			new AssetDescriptor<Sound>("sounds/pickup.wav", Sound.class);
	public static final AssetDescriptor<Sound> main_wav =
			new AssetDescriptor<Sound>("sounds/maintune.wav", Sound.class);
	
	//public static final TiledMap bunker_entrance = 
	/**Load all assets**/
	public static void loadAll(){
		manager.load(font);
		manager.load(items_small);
		manager.load(items_large);
		manager.load(ui);
		manager.load(hungerbarbase);
		manager.load(titleimg);
		manager.load(gameoverimg);
		
		manager.load(death_wav);
		manager.load(gun_wav);
		manager.load(hit_wav);
		manager.load(jump_wav);
		manager.load(pickup_wav);
		manager.load(main_wav);
	}
	
	/**Dispose all assets**/
	public static void disposeAll(){
		manager.dispose();
	}

}
