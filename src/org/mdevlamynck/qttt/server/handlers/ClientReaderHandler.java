package org.mdevlamynck.qttt.server.handlers;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.common.messages.EClient;
import org.mdevlamynck.qttt.common.messages.EServer;
import org.mdevlamynck.qttt.server.Client;
import org.mdevlamynck.qttt.server.GameServer;

public class ClientReaderHandler extends Thread {
	
	private GameServer		server		= null;
	
	private	boolean			quit		= false;
	
	private Client			client		= null;
	
	public ClientReaderHandler(GameServer g, Socket s)
	{
		if(g == null || s == null)
			return;

		System.out.println("Handling Client ...");

		client			= new Client();
		client.socket	= s;
		client.reader	= this;
		
		server			= g;

		try
		{
			client.in	= new Scanner(client.socket.getInputStream());
			client.out	= new PrintWriter(client.socket.getOutputStream(), true);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void run()
	{
		if(server == null || client.socket == null || client.in == null || client.out == null)
			return;

		try
		{
			server.addClient(client);
			/*
			while(!quit && !server.getQuit())
			{
				String message = client.in.nextLine();
				
				System.out.println(message);
				
				if(message.equals(EClient.SERVER_STOP.toString()))
				{
					client.out.println(EServer.SERVER_STOPPING);
					client.out.flush();
					
					quit = true;
					client.socket.close();
					
					server.quit();
				}
			}
			*/
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		
		System.out.println("Handling Client Done");
		server.removeClient(client);
	}

}
