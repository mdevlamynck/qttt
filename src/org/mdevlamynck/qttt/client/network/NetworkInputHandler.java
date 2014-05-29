package org.mdevlamynck.qttt.client.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;
import org.mdevlamynck.qttt.common.network.messages.EServer;

public class NetworkInputHandler extends Thread {
	
	private MainFrame		controller	= null;
	private	Server			server		= new Server();
	private String			address		= null;
	private int				port		= -1;
	
	public NetworkInputHandler(MainFrame win)
	{
		this.controller			= win;
	}
	
	public boolean setServer(String name, String addressServer, int portServer)
	{
		this.server.name	= name;
		this.address		= addressServer;
		this.port			= portServer;
		
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
				controller.lostConnection();
			
			return;
		}

		String line;
		
		while(true)
		{
			try
			{
				line = server.in.nextLine();
				System.out.println(line);

				if		(line.equals(EServer.STOPPING.toString()))
					break;

				else if	(line.startsWith(EPrefixes.GAME.toString()))
					server.game.push(line.substring(EPrefixes.GAME.toString().length()));
	
				else if	(line.startsWith(EPrefixes.CHAT.toString()))
					server.gameChat.push(line.substring(EPrefixes.CHAT.toString().length()));

				else if	(line.startsWith(EPrefixes.LOBBY.toString()))
					server.lobby.push(line.substring(EPrefixes.LOBBY.toString().length()));
			}
			catch(NoSuchElementException e)
			{
				break;
			}
		}

		controller.lostConnection();
	}

	public Server getServer() {
		return server;
	}

}
