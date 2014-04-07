package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.common.messages.EServer;
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
			controller.addToChat(server.chat.pop(), true);;
	}

	public void sendChatMessg(String sendText) {
		server.out.println(EServer.CHAT_PREFIX.toString() + sendText);
	}

}
