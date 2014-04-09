package org.mdevlamynck.qttt.common.network;

import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public abstract class BasicHandler extends Thread {
	
	public EPrefixes	handlerPrefix	= EPrefixes.NONE;

	public abstract void addMessage(OtherEndMessage mess);

	public void writeLine(OtherEnd client, String line)
	{
		client.out.println(handlerPrefix.toString() + line);
	}

}
