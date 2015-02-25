package mw.client.controller;

import java.util.HashMap;

import mw.client.model.Tile;

public final class ModelViewMapping {

	
	//	Generic Controller
	
	private static ModelViewMapping singleton = null;
	
	public static void initialize() {
		singleton = new ModelViewMapping();
	}
	
	public static void clear() {
		singleton = null;
	}
	
	public static ModelViewMapping singleton() {
		return singleton;
	}
	
	
	//	ModelViewMapping
	
	private HashMap<Tile, TileDisplay> tileToTileDisplay;
	
	private ModelViewMapping()
	{
		tileToTileDisplay = new HashMap<Tile,TileDisplay>();
	}
	
	public TileDisplay getTileDisplay(Tile t) {
		return tileToTileDisplay.get(t);
	}
	
	public void addBinding(Tile t, TileDisplay td)
	{
		tileToTileDisplay.put(t,td);
	}
	
}
