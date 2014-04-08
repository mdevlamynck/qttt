package org.mdevlamynck.qttt.common.network.datastruct;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.common.network.MessageQueue;
import org.mdevlamynck.qttt.server.handlers.ChatHandler;
import org.mdevlamynck.qttt.server.handlers.ClientReaderHandler;
import org.mdevlamynck.qttt.server.handlers.GameSessionHandler;

public class Client {
	
	public Socket				socket			= null;
	
	public Scanner				in				= null;
	public PrintWriter			out				= null;
	
	public MessageQueue			game			= new MessageQueue();
	public MessageQueue			chat			= new MessageQueue();
	
	public ClientReaderHandler	readerHandler	= null;
	public GameSessionHandler	gameHandler		= null;

}
