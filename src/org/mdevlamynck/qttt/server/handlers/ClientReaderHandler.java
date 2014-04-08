package org.mdevlamynck.qttt.server.handlers;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;
import org.mdevlamynck.qttt.server.GameServer;

public class ClientReaderHandler extends Thread {
	
	private GameServer		server		= null;
	
	private Client			client		= null;
	
	public ClientReaderHandler(GameServer g, Socket s)
	{
		if(g == null || s == null)
			return;

		System.out.println("Handling Client ...");

		client					= new Client();
		client.socket			= s;
		client.readerHandler	= this;
		
		server					= g;
	}

	@Override
	public void run()
	{
		try
		{
			client.in	= new Scanner(client.socket.getInputStream());
			client.out	= new PrintWriter(client.socket.getOutputStream(), true);
		}
		catch(Exception e)
		{
			return;
		}

		if(server == null || client.socket == null || client.in == null || client.out == null)
			return;

		server.addClient(client);
		while(true)
		{

			try
			{
				String message = client.in.nextLine();
				
				if		(message.equals(EMessages.SERVER_STOP.toString()))
				{
					client.out.println(EMessages.SERVER_STOPPING);
					client.out.flush();
					client.socket.close();
					
					server.quit();
				}
				else if	(message.startsWith(EMessages.GAME_PREFIX.toString()))
					client.game.push(message.substring(EMessages.GAME_PREFIX.toString().length()));
				
				else if	(message.startsWith(EMessages.CHAT_PREFIX.toString()))
					client.chat.push(message.substring(EMessages.CHAT_PREFIX.toString().length()));
			}
			catch(Exception e)
			{
				break;
			}
		}	
		
		System.out.println("Handling Client Done");
		server.removeClient(client);
	}

}
