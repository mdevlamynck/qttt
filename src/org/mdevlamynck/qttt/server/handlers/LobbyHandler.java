package org.mdevlamynck.qttt.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.ConcurrentQueue;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EGame;
import org.mdevlamynck.qttt.common.network.messages.ELobby;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;
import org.mdevlamynck.qttt.server.datastructs.Client;

public class LobbyHandler extends BasicHandler {
	
	private	List<GameSessionHandler>			sessions		= new ArrayList<GameSessionHandler>();
	private	List<Client>						clients			= new ArrayList<Client>();
	
	private	Client								waitingClient	= null;
	
	private ConcurrentQueue<OtherEndMessage>	messages		= new ConcurrentQueue<OtherEndMessage>();
	
	public LobbyHandler()
	{
		handlerPrefix	= EPrefixes.LOBBY;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				OtherEndMessage mess = messages.pop();
				if		(mess.message.startsWith(ELobby.ADD_CLIENT.toString()))
					add((Client) mess.client);
				else if (mess.message.startsWith(ELobby.REMOVE_CLIENT.toString()))
					remove((Client) mess.client);
				else if (mess.message.startsWith(ELobby.CLIENT_NAME.toString()))
					mess.client.name = messages.pop().message;
				else if	(mess.message.startsWith(ELobby.REQUEST_SESSION_LIST.toString()))
				{
					String clientsLine = ELobby.REPLY_SESSION_LIST.toString();
					for(Client c : clients)
						clientsLine += c.name + " ";
					writeLine(mess.client, clientsLine);
				}
			}
			catch(InterruptedException e)
			{
				break;
			}
		}
		
		for(GameSessionHandler g : sessions)
			g.interrupt();

		try
		{
			for(GameSessionHandler g : sessions)
				g.join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		sessions.clear();
	}

	public void add(Client client)
	{

		clients.add(client);
		
		if(waitingClient == null)
		{
			waitingClient = client;
		}
		else
		{
			writeLine( waitingClient,	ELobby.START.toString()	);
			writeLine( client,			ELobby.START.toString()	);
			
			GameSessionHandler g	= new GameSessionHandler(waitingClient, client);
			waitingClient			= null;
			
			sessions.add(g);
			g.start();
		}
		
	}

	public void remove(Client client)
	{
		if(clients.remove(client))
		{
			if(client.readerHandler != null)
				client.readerHandler.interrupt();

			if(client.gameHandler != null)
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

	@Override
	public void addMessage(OtherEndMessage mess) {
		messages.push(mess);
	}

}
