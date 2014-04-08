package org.mdevlamynck.qttt.server.handlers;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EServer;
import org.mdevlamynck.qttt.server.GameServer;
import org.mdevlamynck.qttt.server.datastructs.Client;

public class NetworkInputHandler extends Thread {

	private GameServer		server		= null;

	private Client			client		= null;

	public NetworkInputHandler(GameServer g, Socket s)
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

				if		(message.equals(EServer.STOP.toString()))
				{
					client.out.println(EServer.STOPPING);
					client.out.flush();
					client.socket.close();

					server.quit();
				}
				else if	(client.gameHandler != null && message.startsWith(client.gameHandler.handlerPrefix.toString()))
					client.gameHandler.addMessage(new OtherEndMessage(
						client,
						message.substring(client.gameHandler.handlerPrefix.toString().length())
					));

				else if	(client.gameChatHandler != null && message.startsWith(client.gameChatHandler.handlerPrefix.toString()))
					client.gameChatHandler.addMessage(new OtherEndMessage(
						client,
						message.substring(client.gameChatHandler.handlerPrefix.toString().length())	
					));
				else if	(client.lobbyHandler != null && message.startsWith(client.lobbyHandler.handlerPrefix.toString()))
					client.lobbyHandler.addMessage(new OtherEndMessage(
						client,
						message.substring(client.lobbyHandler.handlerPrefix.toString().length())	
					));
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
