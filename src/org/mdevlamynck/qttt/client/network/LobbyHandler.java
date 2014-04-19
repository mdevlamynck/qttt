package org.mdevlamynck.qttt.client.network;

import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EGame;
import org.mdevlamynck.qttt.common.network.messages.ELobby;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public class LobbyHandler extends BasicHandler {

	private LobbyClient	controller	= null;
	private	Server		server		= null;

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
		List<String> sessions	= new ArrayList<String>();

		for(String s : sessionsLine.split(" "))
		{
			if(!s.isEmpty())
				sessions.add(s);
		}

		controller.setSession(sessions);
	}

	private void updateClientList(String clientsLine)
	{
		List<String> clients	= new ArrayList<String>();

		for(String s : clientsLine.split(" "))
		{
			if(!s.isEmpty())
				clients.add(s);
		}

		controller.setClient(clients);
	}

	public void createGame()
	{
		writeLine(server, ELobby.CREATE_SESSION.toString());
	}

}
