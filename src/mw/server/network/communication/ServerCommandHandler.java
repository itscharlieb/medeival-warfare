/**
 * @author Charlie Bloomfield
 * Feb 20, 2015
 * 
 */
package mw.server.network.communication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import mw.server.network.mappers.ClientChannelMapper;
import mw.shared.clientcommands.ErrorMessageCommand;
import mw.shared.servercommands.AbstractServerCommand;

/** 
 * @singleton
 * Creates a thread which waits for AbstractServerCommands to become available in a BlockingQueue.
 * It calls the verify method on each AbstractServerCommand, and if valid, calls execute on each message.
 * 
 * TODO
 * If the AbstractServerCommand is not valid, it must initiate forwarding a message to the
 * client who sent the AbstractServerCommand that informs the User the reason the AbstractServerCommand
 * is invalid.
 */
public class ServerCommandHandler {
	
	private static ServerCommandHandler aServerCommandHandler;
	private BlockingQueue<ServerCommandWrapper> aServerCommandQueue;
	
	/**
	 * Constructor. Initiates a ServerCommandHandlerThread, which exists for the entire execution
	 * of the server program.
	 */
	private ServerCommandHandler(){
		aServerCommandQueue = new LinkedBlockingQueue<ServerCommandWrapper>();
		ServerCommandHandlerThread lServerCommandHandlerThread = new ServerCommandHandlerThread();
		lServerCommandHandlerThread.start();
	}
	
	/**
	 * Singleton implementation
	 * @return the static instance of ServerCommandHandler
	 */
	public static ServerCommandHandler getInstance(){
		if(aServerCommandHandler == null){
			aServerCommandHandler = new ServerCommandHandler();
		}
		
		return aServerCommandHandler;
	}
	
	/**
	 * Enqueues pServerCommand to be serviced when the ServerCommandHandlerThread
	 * is available to service it.
	 * @param pServerCommand, the server message to be handled
	 * @param pClientID, the identification number of the requesting Client.
	 */
	public synchronized void handle(AbstractServerCommand pServerCommand, int pClientID){
		try {
			aServerCommandQueue.put(
					new ServerCommandWrapper(pServerCommand, pClientID));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Nested Thread class ServerCommandHandlerThread handles all ServerCommands, for all games.
	 */
	class ServerCommandHandlerThread extends Thread {
		public void run(){
			while(true){
				try {
					ServerCommandWrapper lServerCommandWrapper = aServerCommandQueue.take();
					AbstractServerCommand lServerCommand = lServerCommandWrapper.getServerCommand();
					int lClientID = lServerCommandWrapper.getClientID();
					
					try {
						lServerCommand.execute(lClientID);
					} catch (Exception e) {
						e.printStackTrace();
						ClientChannelMapper.getInstance().getChannel(lClientID).sendCommand(
								new ErrorMessageCommand(e.getMessage()));
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Nested class maintains the ClientID attribute.
	 */
	class ServerCommandWrapper{
		AbstractServerCommand aAbstractServerCommand;
		int aClientID;
		
		ServerCommandWrapper(AbstractServerCommand pServerCommand, int pClientID) {
			aAbstractServerCommand = pServerCommand;
			aClientID = pClientID;
		}
		
		public AbstractServerCommand getServerCommand(){
			return aAbstractServerCommand;
		}
		
		public int getClientID(){
			return aClientID;
		}
	}
}
