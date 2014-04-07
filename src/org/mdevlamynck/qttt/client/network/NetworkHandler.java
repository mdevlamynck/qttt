package org.mdevlamynck.qttt.client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.mdevlamynck.qttt.client.gui.MainPanel;
import org.mdevlamynck.qttt.common.gamelogic.Turn;
import org.mdevlamynck.qttt.common.gamelogic.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.messages.EServer;

public class NetworkHandler extends Thread {
	
	private MainPanel	win		= null;
	private Socket		server	= null;
	private	Scanner		in		= null;
	private	PrintWriter	out		= null;
	private EPlayer		player	= EPlayer.NotYetPlayed;
	
	public NetworkHandler(MainPanel win)
	{
		this.win = win;
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
					writeLine		( win.getTurn().toString()				);
				
				else if	( line.equals(EServer.GAME_REQUEST_CHOICE.toString())	)
					writeLine		( ((Integer)win.getChoice()).toString()	);
				
				else if	( line.equals(EServer.GAME_OTHER_TURN.toString())		)
					win.addTurn		( new Turn().fromString(readLine()) 	);
				
				else if	( line.equals(EServer.GAME_OTHER_CHOICE.toString())		)
					win.addChoice	( Integer.parseInt(readLine()) 			);
				
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
		win.addToLog("< " + line);
		return line;
	}
	
	private void writeLine(String line)
	{
		out.println(line);
		win.addToLog("> " + line);
	}

}
