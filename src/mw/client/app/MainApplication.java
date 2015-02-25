package mw.client.app;

import java.util.ArrayList;

import mw.client.controller.ActionInterpreter;
import mw.client.controller.ModelViewMapping;
import mw.client.controller.TileModificationHandler;
import mw.client.gui.GameWindow;
import mw.client.gui.MapDisplay;
import mw.client.gui.ImageTile;
import mw.client.model.Game;
import mw.client.model.GameMap;
import mw.client.model.Player;
import mw.client.model.Tile;

public final class MainApplication {

	private static final Player PLAYER = null;
	private final static int MAP_WIDTH = 10;
	private final static int MAP_HEIGHT = 10;
	
	private static GameWindow window;
	private static Game game;
	private static Tile randomTile;
	
	public static void main(String[] args)
	{
		newGame();
		window.render();
		System.out.println("Rendered !");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Slept");
		randomTile.setRoad(true);
		System.out.println("set road");
		randomTile.notifyObservers();
		System.out.println("notified");
		//window.update();
	}
	
	public static Game newGame()
	{
		TileModificationHandler observer = new TileModificationHandler();
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		ImageTile displayedTiles[][] = new ImageTile[MAP_WIDTH][MAP_HEIGHT];
		ModelViewMapping.initialize();
		ModelViewMapping mapping = ModelViewMapping.singleton();
		
		for (int i=0; i<MAP_WIDTH; i++)
		{
			for (int j=0; j<MAP_HEIGHT; j++)
			{
				Tile t = new Tile(i, j);
				tileList.add(t);
				t.addObserver(observer);
				if (i==2 && j==1)
					randomTile = t;
				
				ImageTile td = new ImageTile(/*...*/);
				displayedTiles[i][j] = td;
				mapping.addBinding(t, td);
			}
		}
		
		GameMap m = new GameMap(tileList);
		game = new Game(m, PLAYER);
		
		MapDisplay mapdisp = new MapDisplay(displayedTiles);
		window = new GameWindow(mapdisp);
		
		//ActionInterpreter.initialize(game);
		return game;
	}
	
}