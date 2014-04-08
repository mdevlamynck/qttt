package org.mdevlamynck.qttt.server.datastructs;

import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;
import org.mdevlamynck.qttt.server.handlers.ChatHandler;
import org.mdevlamynck.qttt.server.handlers.NetworkInputHandler;
import org.mdevlamynck.qttt.server.handlers.GameSessionHandler;
import org.mdevlamynck.qttt.server.handlers.LobbyHandler;

public class Client extends OtherEnd {
	
	public NetworkInputHandler	readerHandler	= null;
	public GameSessionHandler	gameHandler		= null;
	public ChatHandler			gameChatHandler	= null;
	public LobbyHandler			lobbyHandler	= null;

}
