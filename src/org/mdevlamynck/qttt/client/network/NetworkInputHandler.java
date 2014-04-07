package org.mdevlamynck.qttt.client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.client.gui.MainPanel;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.messages.EServer;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class NetworkInputHandler extends Thread {
	
	private GameClient	controller	= null;
	private	Client		server		= null;
	
	public NetworkInputHandler(GameClient win, Client server)
	{
		this.controller	= win;
		this.server		= server;
	}
	
	@Override
	public void run()
	{
		try
		{
			server.socket	= new Socket("localhost", 42042);
			server.in		= new Scanner(server.socket.getInputStream());
			server.out		= new PrintWriter(server.socket.getOutputStream(), true);
			
			String line;
			
			while(!controller.getQuit())
			{
				line = server.in.nextLine();
				
				if		(line.startsWith(EServer.GAME_PREFIX.toString()))
					server.game.push(line.substring(EServer.GAME_PREFIX.toString().length()));

				else if	(line.startsWith(EServer.CHAT_PREFIX.toString()))
					server.chat.push(line.substring(EServer.CHAT_PREFIX.toString().length()));
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

}
