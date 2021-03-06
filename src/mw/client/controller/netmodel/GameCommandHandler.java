package mw.client.controller.netmodel;

import mw.client.controller.CurrentClientState;
import mw.client.controller.guimodel.DisplayUpdater;
import mw.client.controller.guimodel.GameInitializer;
import mw.client.controller.menuing.MenuControl;
import mw.client.controller.menuing.ScreenSwitcher;
import mw.client.controller.menuing.ScreenSwitcher.ScreenKind;
import mw.client.controller.model.NewStateApplier;
import mw.client.controller.translator.NetworkModelTranslator;
import mw.client.model.ModelTile;
import mw.shared.SharedGameLobby;
import mw.shared.SharedTile;


/**
 * The GameCommandHandler controller is responsible for handling the different commands that
 * could come from the server, modifying accordingly the model on the client
 * @author Hugo Kapp
 *
 */
public final class GameCommandHandler {

	/**
	 * Creates a new Game out of the given SharedTiles, and start displaying and interacting with the User
	 * @param newTiles the SharedTiles that give the initial state for each Tile in the new Game
	 */
	public static void createNewGame(SharedTile[][] newTiles)
	{
		ClientSynchronization.gameLock.lock();
		
		final int width = newTiles.length;
		final int height = newTiles[0].length;
		ModelTile[][] gameTiles = new ModelTile[width][height];
		for (int i=0; i<width; i++)
		{
			for (int j=0; j<height; j++)
			{
				gameTiles[i][j] = NetworkModelTranslator.translateSharedTile(newTiles[i][j]);
			}
		}
		
		GameInitializer.newGame(gameTiles);
		
		ScreenSwitcher.switchScreen(ScreenKind.NONE);
		
		ClientSynchronization.gameLock.unlock();
	}
	
	/**
	 * Applies the new state of the given SharedTile to the model
	 * @param newState the new state of a Tile
	 */
	public static void newTileState(SharedTile newState)
	{
		ClientSynchronization.gameLock.lock();
		if (CurrentClientState.isInGame())
			NewStateApplier.applyChanges(CurrentClientState.getCurrentGame(), newState);
		ClientSynchronization.gameLock.unlock();
	}
	
	public static void setNowPlaying(boolean nowPlaying)
	{
		ClientSynchronization.gameLock.lock();
		CurrentClientState.getCurrentGame().setPlaying(nowPlaying);
		DisplayUpdater.showEndTurnButton(nowPlaying);
		ClientSynchronization.gameLock.unlock();
	}
	
	public static void displayMessage(String message) {
		if (message == null)
			message = "";
		ClientSynchronization.gameLock.lock();
		DisplayUpdater.showGeneralMessage(message);
		ClientSynchronization.gameLock.unlock();
	}
	
	public static void leaveGame(SharedGameLobby lobby) {
		ClientSynchronization.gameLock.lock();
		
		CurrentClientState.setCurrentGame(null);
		CurrentClientState.getCurrentGameWindow().close();
		CurrentClientState.setCurrentGameWindow(null);
		
		ClientSynchronization.gameLock.unlock();
		
		ScreenSwitcher.openLobbyScreen(lobby);
	}
	
}