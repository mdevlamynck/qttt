package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.messages.EServer;
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
		do
		{
			line = readLine();
			
			if		( line.equals(EServer.GAME_START.toString())			)
				player = Integer.parseInt(readLine()) == 1 ? EPlayer.P1 : EPlayer.P2;
		
			else if	( line.equals(EServer.GAME_REQUEST_TURN.toString())		)
				writeLine	( controller.getTurn().toString()				);
			
			else if	( line.equals(EServer.GAME_REQUEST_CHOICE.toString())	)
				writeLine	( ((Integer)controller.getChoice()).toString()	);
			
			else if	( line.equals(EServer.GAME_OTHER_TURN.toString())		)
				controller.addTurn		( new Turn().fromString(readLine()) );
			
			else if	( line.equals(EServer.GAME_OTHER_CHOICE.toString())		)
				controller.addChoice	( Integer.parseInt(readLine()) 		);
			
		} while( !line.equals(EServer.GAME_FINISHED.toString())	);
		
		readLine();
	}

	private String readLine()
	{
		String line = server.game.pop();
		controller.addToLog(line, true);
		return line;
	}
	
	private void writeLine(String line)
	{
		server.out.println(EServer.GAME_PREFIX.toString() + line);
		controller.addToLog(line, false);
	}
	
}
