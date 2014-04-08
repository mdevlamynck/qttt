package org.mdevlamynck.qttt.client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class NetworkInputHandler extends Thread {
	
	private MainFrame	controller	= null;
	private	Client		server		= new Client();
	private String		address		= null;
	private int			port		= -1;
	
	public NetworkInputHandler(MainFrame win)
	{
		this.controller	= win;
	}
	
	public boolean setServer(String addressServer, int portServer)
	{
		this.address	= addressServer;
		this.port		= portServer;
		
		try
		{
			server.socket	= new Socket(address, port);
			server.in		= new Scanner(server.socket.getInputStream());
			server.out		= new PrintWriter(server.socket.getOutputStream(), true);			
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public void run()
	{
		if(controller == null || address == null || port == -1)
		{
			if(controller != null)
				controller.getCurrent().quit();
			
			return;
		}

		String line;
		
		while(true)
		{
			try
			{
				line = server.in.nextLine();

				if		(line.equals(EMessages.SERVER_STOPPING.toString()))
					break;
				
				else if	(line.startsWith(EMessages.GAME_PREFIX.toString()))
					server.game.push(line.substring(EMessages.GAME_PREFIX.toString().length()));
	
				else if	(line.startsWith(EMessages.CHAT_PREFIX.toString()))
					server.chat.push(line.substring(EMessages.CHAT_PREFIX.toString().length()));
			}
			catch(NoSuchElementException e)
			{
				break;
			}
		}

		controller.lostConnection();
	}

	public Client getServer() {
		return server;
	}

}
