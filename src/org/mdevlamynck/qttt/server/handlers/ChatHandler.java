package org.mdevlamynck.qttt.server.handlers;

import org.mdevlamynck.qttt.common.messages.EServer;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class ChatHandler extends Thread {
	
	private Client			p1;
	private Client			p2;
	
	public ChatHandler(Client p1, Client p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			String line = p1.chat.pop();
			writeLine(p2, line);
		}
	}

	private void writeLine(Client client, String line)
	{
		client.out.println(EServer.CHAT_PREFIX.toString() + line);
	}

}
