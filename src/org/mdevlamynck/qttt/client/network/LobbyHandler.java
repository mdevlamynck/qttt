package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EGame;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public class LobbyHandler extends BasicHandler {
	
	private LobbyClient				controller	= null;
	private	Server					server		= null;
	
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
		
		while(true)
		{
			try
			{
				line = server.lobby.pop();

				if(line.contains(EGame.START.toString()))
				{
					controller.toGame();
					break;	
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
	public void addMessage(OtherEndMessage mess) {
		server.lobby.push(mess.message);
	}

}
