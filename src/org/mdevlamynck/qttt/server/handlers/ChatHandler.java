package org.mdevlamynck.qttt.server.handlers;

import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class ChatHandler extends Thread {
	
	private GameSessionHandler	game	= null;
	private Client				p1		= null;
	private Client				p2		= null;
	
	public ChatHandler(GameSessionHandler game, Client p1, Client p2)
	{
		this.game	= game;
		this.p1		= p1;
		this.p2		= p2;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				String line = p1.chat.pop();
				writeLine(p2, line);
			}
			catch(InterruptedException e)
			{
				break;
			}
		}
	}

	private void writeLine(Client client, String line)
	{
		client.out.println(EMessages.CHAT_PREFIX.toString() + line);
	}

}
