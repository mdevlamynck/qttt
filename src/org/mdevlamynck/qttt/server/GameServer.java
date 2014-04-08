package org.mdevlamynck.qttt.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;
import org.mdevlamynck.qttt.server.handlers.ClientReaderHandler;
import org.mdevlamynck.qttt.server.handlers.GameSessionHandler;

public class GameServer {
	
	private ServerSocket				ssock			= null;
	private boolean						quit			= false;

	private	List<Client>				clients			= new ArrayList<Client>();
	private	List<GameSessionHandler>	sessions		= new ArrayList<GameSessionHandler>();
	
	private	Client						waitingClient	= null;
	
	private Object						clientsLock		= new Object();
	
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
				new ClientReaderHandler(this, ssock.accept()).start();
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void quit()
	{
		if(quit)
			return;
		
		quit = true;
		
		try
		{
			Socket			server	= new Socket("localhost", 42042);
			PrintWriter		out		= new PrintWriter(server.getOutputStream(), true);
			
			out.println(EMessages.EMPTY_MESSAGE);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void addClient(Client client)
	{
		synchronized (clientsLock)
		{
			clients.add(client);
			
			if(waitingClient == null)
				waitingClient = client;
			else
			{
				GameSessionHandler g	= new GameSessionHandler(waitingClient, client);
				waitingClient			= null;
				
				sessions.add(g);
				g.start();
			}
		}
	}
	
	public void removeClient(Client client)
	{
		synchronized (clientsLock)
		{
			if(clients.remove(client))
			{
				client.readerHandler.interrupt();
				client.gameHandler.interrupt();

				try
				{
					client.socket.close();
				}
				catch(Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	public boolean getQuit()
	{
		return quit;
	}

}
