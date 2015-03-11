package test.mw.server.gamelogic;

import static org.junit.Assert.*;

import java.util.Iterator;

import mw.server.gamelogic.Game;
import mw.server.gamelogic.GameController;
import mw.server.gamelogic.PossibleGameActions;
import mw.server.gamelogic.Tile;
import mw.server.gamelogic.TooManyPlayersException;
import mw.util.MultiArrayIterable;

import org.junit.Test;

import com.google.gson.Gson;

public class TestGetPossibleGameActions {

	@Test
	public void test() {
		Gson gson = new Gson();
		Game lGame;
		try {
			lGame = GameController.newGame(2);
		} catch (TooManyPlayersException e) {
			System.out.println("[Test] Cannot create a game with that many players.");
			return;
		}
		
		Tile[][] lGameTiles = lGame.getGameTiles();
		
		for(Tile lTile : MultiArrayIterable.toIterable(lGameTiles)){
			PossibleGameActions lPossibleGameActions = lGame.tileIsClicked(lTile);
			
			System.out.println(new Gson().toJson(lPossibleGameActions.getMovableTiles()));
		}
	}

}