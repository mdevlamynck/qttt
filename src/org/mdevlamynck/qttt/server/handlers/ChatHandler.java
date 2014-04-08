package org.mdevlamynck.qttt.server.handlers;

import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.ConcurrentQueue;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;
import org.mdevlamynck.qttt.server.datastructs.Client;

public class ChatHandler extends BasicHandler {

	private Client							p1			= null;
	private Client							p2			= null;
	private	ConcurrentQueue<OtherEndMessage>	messages	= new ConcurrentQueue<OtherEndMessage>();
	
	public ChatHandler(Client p1, Client p2)
	{
		handlerPrefix	= EPrefixes.CHAT;

		this.p1		= p1;
		this.p2		= p2;
		
		p1.gameChatHandler	= this;
		p2.gameChatHandler	= this;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				OtherEndMessage mess = messages.pop();
				
				if		(mess.client.equals(p1))
					writeLine(p2, mess.message);
				else if	(mess.client.equals(p2))
					writeLine(p1, mess.message);
			}
			catch(InterruptedException e)
			{
				break;
			}
		}
	}
	
	@Override
	public void addMessage(OtherEndMessage mess)
	{
		messages.push(mess);
	}

}
