package org.mdevlamynck.qttt.common.network.datastruct;


public class OtherEndMessage {
	
	public OtherEnd	client	= null;
	public String	message	= null;
	
	public OtherEndMessage(OtherEnd client, String message)
	{
		this.client		= client;
		this.message	= message;
	}

}
