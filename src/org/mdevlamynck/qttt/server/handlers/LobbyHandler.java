package org.mdevlamynck.qttt.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.ConcurrentQueue;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;
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
					add( (Client) mess.client );

				else if (mess.message.startsWith(ELobby.REMOVE_CLIENT.toString()))
					remove( (Client) mess.client );

				else if (mess.message.startsWith(ELobby.CLIENT_NAME.toString()))
					mess.client.name = mess.message.substring(ELobby.CLIENT_NAME.toString().length());

				else if	(mess.message.startsWith(ELobby.REQUEST_CLIENT_LIST.toString()))
					requestClientList( (Client) mess.client );

				else if	(mess.message.startsWith(ELobby.REQUEST_SESSION_LIST.toString()))
					requestSessionList( (Client) mess.client );

				else if	(mess.message.startsWith(ELobby.CREATE_SESSION.toString()))
					requestCreateGame( (Client) mess.client );
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
		/*
		   if(waitingClient == null)
		   waitingClient = client;
		   else
		   {
		   createGame(waitingClient, client);
		   waitingClient = null;
		   }
		   */
	}

	public void remove(Client client)
	{
		if(clients.remove(client))
		{
			if(client.readerHandler != null)
				client.readerHandler.interrupt();

			if(client.gameHandler != null)
				client.gameHandler.interrupt();

			sessions.remove(client.gameHandler);

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

	public void createGame(Client c1, Client c2)
	{
		writeLine( c1,	ELobby.START.toString()	);
		writeLine( c2,	ELobby.START.toString()	);

		GameSessionHandler g	= new GameSessionHandler(c1, c2);

		sessions.add(g);
		g.start();
	}

	private void requestClientList(Client client)
	{
		String clientsLine = ELobby.REPLY_CLIENT_LIST.toString();
		for(Client c : clients)
		{
			if(!client.equals(c))
				clientsLine += c.name + " ";
		}
		writeLine(client, clientsLine);
	}

	private void requestSessionList(Client client)
	{
		String clientsLine = ELobby.REPLY_SESSION_LIST.toString();
		for(GameSessionHandler s : sessions)
		{
			if(!s.isAlive())
				clientsLine += s.getClient1().name + " ";
		}
		writeLine(client, clientsLine);
	}

	public void requestCreateGame(Client client)
	{
		GameSessionHandler	newGame	= new GameSessionHandler();
		newGame.setClient1(client);

		sessions.add(newGame);
	}

	@Override
	public void addMessage(OtherEndMessage mess)
	{
		messages.push(mess);
	}

}
