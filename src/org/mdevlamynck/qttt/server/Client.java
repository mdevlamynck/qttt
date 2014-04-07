package org.mdevlamynck.qttt.server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.server.handlers.ClientReaderHandler;
import org.mdevlamynck.qttt.server.handlers.GameSessionHandler;

public class Client {
	
	public Socket				socket;
	
	public Scanner				in;
	public PrintWriter			out;
	
	public ClientReaderHandler	reader;
	public GameSessionHandler	session;

}
