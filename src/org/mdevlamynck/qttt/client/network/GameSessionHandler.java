package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.network.EMessages;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class GameSessionHandler extends Thread {
	
	private GameClient	controller	= null;
	private	Client		server		= null;
	private EPlayer		player		= EPlayer.NotYetPlayed;
	
	public GameSessionHandler(GameClient controller, Client server)
	{
		this.controller	= controller;
		this.server		= server;
	}
	
	@Override
	public void run()
	{
		String line = null;
		
		while(!controller.getQuit())
		{
			try
			{
				line = readLine();
				
				if		( line.equals(EMessages.GAME_START.toString())			)
					player = Integer.parseInt(readLine()) == 1 ? EPlayer.P1 : EPlayer.P2;
			
				else if	( line.equals(EMessages.GAME_REQUEST_TURN.toString())		)
					writeLine	( controller.getTurn().toString()				);
				
				else if	( line.equals(EMessages.GAME_REQUEST_CHOICE.toString())	)
					writeLine	( ((Integer)controller.getChoice()).toString()	);
				
				else if	( line.equals(EMessages.GAME_OTHER_TURN.toString())		)
					controller.addTurn		( new Turn().fromString(readLine()) );
				
				else if	( line.equals(EMessages.GAME_OTHER_CHOICE.toString())		)
					controller.addChoice	( Integer.parseInt(readLine()) 		);
				else if	( line.equals(EMessages.GAME_FINISHED.toString())		)
				{
					readLine();
					controller.quit();
				}
			}
			catch(InterruptedException e)
			{
			}
		}
		
		controller.quit();
	}

	private String readLine() throws InterruptedException
	{
		String line = "";
		line = server.game.pop();

		controller.addToLog(line, true);
		return line;
	}
	
	private void writeLine(String line)
	{
		server.out.println(EMessages.GAME_PREFIX.toString() + line);
		controller.addToLog(line, false);
	}
	
}
