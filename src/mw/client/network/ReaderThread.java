package mw.client.network;

import java.io.DataInputStream;
import java.util.concurrent.BlockingQueue;

import mw.shared.networkmessages.AbstractNetworkMessage;

public class ReaderThread extends Thread {
	DataInputStream aDataInputStream;
	BlockingQueue<AbstractNetworkMessage> aServerMessageQueue;
	
	public ReaderThread(DataInputStream pDataInputStream){
		aDataInputStream = pDataInputStream;
	}
	
	public void run(){
		try{
			while(true){
				String lMessageBeingRead = aDataInputStream.readUTF();
				System.out.println("[Client] Message received: " + lMessageBeingRead);
			}
		}
		catch(Exception e){
			System.out.println("[Client] Error sending message.");
			e.printStackTrace();
		}
	}
}
