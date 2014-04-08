package org.mdevlamynck.qttt.client.network.datastructs;

import org.mdevlamynck.qttt.common.network.ConcurrentQueue;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;

public class Server extends OtherEnd {
	
	public ConcurrentQueue<String>	network		= new ConcurrentQueue<String>();
	public ConcurrentQueue<String>	lobby		= new ConcurrentQueue<String>();
	public ConcurrentQueue<String>	game		= new ConcurrentQueue<String>();
	public ConcurrentQueue<String>	gameChat	= new ConcurrentQueue<String>();

}
