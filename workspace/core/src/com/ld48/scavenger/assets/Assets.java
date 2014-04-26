package com.ld48.scavenger.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	//public static final AssetDescriptor<TiledMap> bunker_entrance2 =
	//		new AssetDescriptor<TiledMap>("maps/fightlevel.png", TiledMap.class);
	
	//public static final TiledMap bunker_entrance = 
	/**Load all assets**/
	public static void loadAll(){
		//manager.load(bunker_entrance);
	}
	
	/**Dispose all assets**/
	public static void disposeAll(){
		manager.dispose();
	}

}
