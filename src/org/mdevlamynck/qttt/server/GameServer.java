package org.mdevlamynck.qttt.server;

import java.net.ServerSocket;

import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.ELobby;
import org.mdevlamynck.qttt.server.datastructs.Client;
import org.mdevlamynck.qttt.server.handlers.LobbyHandler;
import org.mdevlamynck.qttt.server.handlers.NetworkInputHandler;

public class GameServer {
	
	private ServerSocket				ssock			= null;
	private boolean						quit			= false;
	
	private LobbyHandler				lobbyHandler	= new LobbyHandler();
	
	public GameServer()
	{
	}
	
	public void start()
	{
		init();
		
		listen();
		
		release();
	}
	
	public void init()
	{
		System.out.println("Server Init ...");
		try
		{
			ssock	= new ServerSocket(42042);
			lobbyHandler.start();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		System.out.println("Server Init Done");
	}
	
	public void release()
	{
		System.out.println("Server Release ...");
		try
		{
			ssock.close();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		System.out.println("Server Release Done");
	}
	
	public void listen()
	{
		try
		{
			while(!quit)
			{
				System.out.println("Server Listening ...");
				new NetworkInputHandler(this, ssock.accept()).start();
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void quit()
	{
		lobbyHandler.interrupt();
	}
	
	public void addClient(Client client)
	{
		lobbyHandler.addMessage(new OtherEndMessage(client, ELobby.ADD_CLIENT.toString()));
	}
	
	public void removeClient(Client client)
	{
		lobbyHandler.addMessage(new OtherEndMessage(client, ELobby.REMOVE_CLIENT.toString()));
	}
	
	public boolean getQuit()
	{
		return quit;
	}

}
