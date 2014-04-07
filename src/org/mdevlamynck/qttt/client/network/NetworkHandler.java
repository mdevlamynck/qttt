package org.mdevlamynck.qttt.client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.client.gui.MainPanel;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.messages.EServer;

public class NetworkHandler extends Thread {
	
	private GameClient	client	= null;
	private Socket		server	= null;
	private	Scanner		in		= null;
	private	PrintWriter	out		= null;
	private EPlayer		player	= EPlayer.NotYetPlayed;
	
	public NetworkHandler(GameClient win)
	{
		this.client = win;
	}
	
	@Override
	public void run()
	{
		try
		{
			server		= new Socket("localhost", 42042);
			in			= new Scanner(server.getInputStream());
			out			= new PrintWriter(server.getOutputStream(), true);
			
			String line;
			
			do
			{
				line = readLine();
				
				if		( line.equals(EServer.GAME_START.toString())			)
					player = Integer.parseInt(readLine()) == 1 ? EPlayer.P1 : EPlayer.P2;
				
				else if	( line.equals(EServer.GAME_REQUEST_TURN.toString())		)
					writeLine		( client.getTurn().toString()				);
				
				else if	( line.equals(EServer.GAME_REQUEST_CHOICE.toString())	)
					writeLine		( ((Integer)client.getChoice()).toString()	);
				
				else if	( line.equals(EServer.GAME_OTHER_TURN.toString())		)
					client.addTurn		( new Turn().fromString(readLine()) 	);
				
				else if	( line.equals(EServer.GAME_OTHER_CHOICE.toString())		)
					client.addChoice	( Integer.parseInt(readLine()) 			);
				
			} while( !line.equals(EServer.GAME_FINISHED.toString())	);
			
			line = readLine();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	private String readLine()
	{
		String line = in.nextLine();
		client.addToLog("< " + line);
		return line;
	}
	
	private void writeLine(String line)
	{
		out.println(line);
		client.addToLog("> " + line);
	}

}
