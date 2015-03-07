package mw.client.network;

import mw.shared.SharedCoordinates;
import mw.shared.servercommands.AbstractServerCommand;
import mw.shared.servercommands.GetPossibleGameActionsCommand;
import mw.shared.servercommands.NewGameRequestCommand;

public class NetworkController {
	private static ServerChannel aServerChannel;
	
	/**
	 * initializes aServerChannel
	 * @param pServerChannel
	 */
	public static void initializeServerChannel(ServerChannel pServerChannel){
		aServerChannel = pServerChannel;
	}
	
	
	/**
	 * 
	 */
	public static void requestNewGame(){
		aServerChannel.sendCommand(new NewGameRequestCommand());
	}
	
	/**
	 * Does something
	 * @param pAbstractServerCommand
	 */
	public static void getPossibleMoves(SharedCoordinates pSharedCoordinates){
		aServerChannel.sendCommand(new GetPossibleGameActionsCommand(pSharedCoordinates));
	}
}
