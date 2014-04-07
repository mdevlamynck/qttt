package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class ChatHandler extends Thread {
	
	private GameClient	controller	= null;
	private	Client		server		= null;
	
	public ChatHandler(GameClient controller, Client server)
	{
		this.controller	= controller;
		this.server		= server;
	}
	
	@Override
	public void run()
	{
		while(!controller.getQuit())
		{
			try
			{
				controller.addToChat(server.chat.pop(), true);
			}
			catch(InterruptedException e)
			{
			}
		}
			
	}

	public void sendChatMessg(String sendText) {
		server.out.println(EMessages.CHAT_PREFIX.toString() + sendText);
	}

}
