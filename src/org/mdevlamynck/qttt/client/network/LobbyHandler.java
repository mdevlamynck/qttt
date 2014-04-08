package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class LobbyHandler extends Thread {
	
	private LobbyClient	controller	= null;
	private	Client		server		= null;
	
	public LobbyHandler(LobbyClient controller, Client server)
	{
		this.controller	= controller;
		this.server		= server;
	}
	
	@Override
	public void run()
	{
		String line;
		
		while(!controller.getQuit())
		{
			try
			{
				line = server.game.pop();
				if(line.contains(EMessages.GAME_START.toString()))
				{
					controller.toGame();
					break;	
				}
			}
			catch(InterruptedException e)
			{
				controller.toConnection();
				break;
			}
		}
	}

}
