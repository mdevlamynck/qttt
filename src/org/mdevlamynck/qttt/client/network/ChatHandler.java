package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public class ChatHandler extends BasicHandler {
	
	private GameClient				controller	= null;
	private	Server					server		= null;
	
	public ChatHandler(GameClient controller, Server server)
	{
		handlerPrefix			= EPrefixes.CHAT;
		
		this.controller			= controller;
		this.server				= server;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				controller.addToChat(server.gameChat.pop(), true);
			}
			catch(InterruptedException e)
			{
				break;
			}
		}
			
	}

	public void sendChatMessg(String sendText) {
		writeLine(server, sendText);
	}

	@Override
	public void addMessage(OtherEndMessage mess) {
		server.gameChat.push(mess.message);
	}

}
