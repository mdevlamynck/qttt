package org.mdevlamynck.qttt.client.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.ELobby;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public class LobbyHandler extends BasicHandler {

	private LobbyClient		controller		= null;
	private	Server			server			= null;

	private List<OtherEnd>	otherClients	= new ArrayList<OtherEnd>();
	private List<OtherEnd>	gamesSessions	= new ArrayList<OtherEnd>();

	public LobbyHandler(LobbyClient controller, Server server)
	{
		handlerPrefix			= EPrefixes.LOBBY;

		this.controller			= controller;
		this.server				= server;
	}

	@Override
	public void run()
	{
		String line;

		writeLine(server, ELobby.CLIENT_NAME.toString() + server.name);

		while(true)
		{
			try
			{
				line = server.lobby.pop();

				if		(line.startsWith(ELobby.START.toString()))
				{
					controller.toGame();
					break;
				}
				else if	(line.startsWith(ELobby.REPLY_SESSION_LIST.toString()))
				{
					updateSessionList(line.substring(ELobby.REPLY_SESSION_LIST.toString().length()));
				}
				else if	(line.startsWith(ELobby.REPLY_CLIENT_LIST.toString()))
				{
					updateClientList(line.substring(ELobby.REPLY_CLIENT_LIST.toString().length()));
				}
			}
			catch(InterruptedException e)
			{
				controller.toChoose();
				break;
			}
		}
	}

	@Override
	public void addMessage(OtherEndMessage mess)
	{
		server.lobby.push(mess.message);
	}

	public void refreshSessions()
	{
		writeLine(server, ELobby.REQUEST_SESSION_LIST.toString());
	}

	public void refreshClients()
	{
		writeLine(server, ELobby.REQUEST_CLIENT_LIST.toString());
	}

	private void updateSessionList(String sessionsLine)
	{
		OtherEnd	client	= null;

		gamesSessions.clear();

		int i = 0;
		for(String s : sessionsLine.split(" "))
		{
			if(!s.isEmpty())
			{
				if((i % 2) == 0)
				{
					client		= new OtherEnd();
					client.id	= UUID.fromString(s);
				}
				else
				{
					client.name	= s;
					gamesSessions.add(client);
				}

				i++;
			}
		}

		controller.setSession(gamesSessions);
	}

	private void updateClientList(String clientsLine)
	{
		OtherEnd	client	= null;

		otherClients.clear();

		int i = 0;
		for(String s : clientsLine.split(" "))
		{
			if(!s.isEmpty())
			{
				if((i % 2) == 0)
				{
					client		= new OtherEnd();
					client.id	= UUID.fromString(s);
				}
				else
				{
					client.name	= s;
					otherClients.add(client);
				}

				i++;
			}
		}

		controller.setClient(otherClients);
	}

	public void createGame()
	{
		writeLine(server, ELobby.CREATE_SESSION.toString());
	}

	public void joinGame(int selectedRow) {
		OtherEnd gameSession	= gamesSessions.get(selectedRow);

		writeLine(server, ELobby.CONNECT_TO_GAME.toString() + gameSession.id.toString());
	}

}
