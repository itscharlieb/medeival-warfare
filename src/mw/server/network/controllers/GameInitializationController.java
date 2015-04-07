/**
 * @author Charlie Bloomfield
 * Mar 5, 2015
 */

package mw.server.network.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import test.mw.server.gamelogic.SaveGame;
import mw.server.admin.Account;
import mw.server.admin.AccountGameInfo;
import mw.server.admin.AccountManager;
import mw.server.gamelogic.controllers.GameController;
import mw.server.gamelogic.enums.Color;
import mw.server.gamelogic.exceptions.TooManyPlayersException;
import mw.server.gamelogic.state.Game;
import mw.server.gamelogic.state.GameID;
import mw.server.gamelogic.state.Player;
import mw.server.gamelogic.state.Tile;
import mw.server.network.communication.ClientCommunicationController;
import mw.server.network.lobby.GameLobby;
import mw.server.network.lobby.GameRoom;
import mw.server.network.lobby.LoadableGameRoom;
import mw.server.network.mappers.GameMapper;
import mw.server.network.mappers.PlayerMapper;
import mw.server.network.translators.LobbyTranslator;
import mw.server.network.translators.SharedTileTranslator;
import mw.shared.SharedGameLobby;
import mw.shared.clientcommands.AbstractClientCommand;
import mw.shared.clientcommands.AcknowledgementCommand;
import mw.shared.clientcommands.DisplayGameLobbyCommand;
import mw.shared.clientcommands.NotifyBeginTurnCommand;
import mw.shared.clientcommands.SetColorCommand;
import mw.util.MultiArrayIterable;
import mw.util.Tuple2;

/**
 * Manages game requests by maintaining a set of game lobbies and creating games when there
 * are sufficient clients available to create a Game. Handles assigning clients to GamePlayers
 * and informing the clients of their Colors.
 */
public class GameInitializationController {
	private static GameInitializationController aGameInitializationController;
	private GameLobby aGameLobby;
	
	/**
	 * Constructor
	 */
	private GameInitializationController(){
		aGameLobby = new GameLobby();
	}

	/**
	 * Singleton implementation
	 * @return static GameInitializationController instance
	 */
	public static GameInitializationController getInstance(){
		if(aGameInitializationController == null){
			aGameInitializationController = new GameInitializationController();
		}

		return aGameInitializationController;
	}
	
	/**
	 * Gets the loaded game, finds out the allowable Account UUIDs and then creates a LoadableGameRoom
	 * @param pAccountID
	 * @param pGameID
	 */
	public void createLoadableGame(UUID pAccountUUID, GameID pGameID){
		Game lGame = pGameID.getaGame();
		int lNumRequestedClients = lGame.getPlayers().size();
		String lLoadedGameName = pGameID.getaName();
				
		LoadableGameRoom lLoadableGameRoom = new LoadableGameRoom(lNumRequestedClients, pGameID);
		aGameLobby.addGameRoom(pGameID.getaName(), lLoadableGameRoom);
			
		//TODO: inform all the clients that they need to join this game
		for(UUID account:lLoadableGameRoom.getClients()){
			if (account!=pAccountUUID) {
				ClientCommunicationController.sendCommand(account, new InviteToLoadedGameCommand());
			}
			else{
				ClientCommunicationController.sendCommand(pAccountUUID, new DisplayNewGameRoomCommand());
			}
		}
	}
	
	/**
	 * @return a set of game lobbies that are open and waiting for players to join
	 */
	public void getJoinableGames(UUID pRequestingAccountID){
		SharedGameLobby lSharedGameLobby = LobbyTranslator.translateGameLobby(aGameLobby);
		ClientCommunicationController.sendCommand(pRequestingAccountID, new DisplayGameLobbyCommand(lSharedGameLobby));
	}

	/**
	 * Creates a new game if there are sufficient Accounts waiting to play. Otherwise, an 
	 * acknowledgement is sent to the Account informing her to wait.
	 * @param pAccountID
	 */
	public void requestNewGame(UUID pRequestingAccountID, String pGameName, int pNumRequestedPlayers){
		aGameLobby.createNewGameRoom(pGameName, pNumRequestedPlayers);
		aGameLobby.addParticipantToGame(pRequestingAccountID, pGameName);
		ClientCommunicationController.sendCommand(pRequestingAccountID, new AcknowledgementCommand("Game \"" + pGameName + "\" was created. Awaiting other players."));
	}
	
	/**
	 * @param pAccountID
	 * @param pGameName
	 */
	public void joinGame(UUID pJoiningAccountID, String pGameName){
		aGameLobby.addParticipantToGame(pJoiningAccountID, pGameName);
		if(aGameLobby.roomIsComplete(pGameName)){
			GameRoom lReadGameRoom = aGameLobby.removeGameRoom(pGameName);
			lReadGameRoom.initializeGame(pGameName);
		}
		else{
			ClientCommunicationController.sendCommand(pJoiningAccountID, new AcknowledgementCommand("Game \"" + pGameName + "\" successfully joined. Awaiting other players"));
		}
	}

	
	/**
	 * Sends an acknowledgement to pAccountID that the game request has been received.
	 * @param pAccountID
	 */
	private void acknowledgeGameRequest(UUID pAccountID){
		AbstractClientCommand lClientCommand =
				new AcknowledgementCommand("Game request received. Insufficient current users. Please wait for more clients to join lobby.");

		ClientCommunicationController.sendCommand(pAccountID, lClientCommand);
	}
	
}
