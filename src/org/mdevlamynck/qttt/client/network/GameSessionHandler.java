package org.mdevlamynck.qttt.client.network;

import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.client.network.datastructs.Server;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EGame;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;

public class GameSessionHandler extends BasicHandler {
	
	private GameClient				controller	= null;
	private	Server					server		= null;
	
	public GameSessionHandler(GameClient controller, Server server)
	{
		handlerPrefix		= EPrefixes.GAME;
		
		this.controller		= controller;
		this.server			= server;
	}
	
	@Override
	public void run()
	{
		String line = null;
		
		while(true)
		{
			try
			{
				line = readLine();
				
				if		( line.startsWith(EGame.START.toString())			)
					controller.setPlayer(
						Integer.parseInt( line.substring(EGame.START.toString().length()) ) == 1
							? EPlayer.P1
							: EPlayer.P2
					);
			
				else if	( line.startsWith(EGame.REQUEST_TURN.toString())	)
				{
					new RequestTurn().start();
				}

				else if	( line.startsWith(EGame.REQUEST_CHOICE.toString())	)
				{
					new RequestChoice().start();
				}
				
				else if	( line.startsWith(EGame.OTHER_TURN.toString())		)
					controller.addTurn(	new Turn().fromString(
						line.substring( EGame.OTHER_TURN.toString().length() )
					));
				
				else if	( line.startsWith(EGame.OTHER_CHOICE.toString())	)
					controller.addChoice( Integer.parseInt(
							line.substring( EGame.OTHER_CHOICE.toString().length() )
						));

				else if	( line.startsWith(EGame.FINISHED.toString())		)
				{
					break;
				}

				else if	( line.startsWith(EGame.INTERRUPTED.toString())		)
				{
					break;
				}
			}
			catch(InterruptedException e)
			{
				break;
			}
		}

		controller.toLobby();
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
		super.writeLine(server, line);
		controller.addToLog(line, false);
	}

	@Override
	public void addMessage(OtherEndMessage mess) {
		server.game.push(mess.message);
	}
	
	private class RequestTurn extends Thread
	{
		@Override
		public void run()
		{
			Turn turn	= controller.getTurn();
			if(turn != null)
				writeLine(
					EGame.REPLY_TURN.toString() +
					turn.toString()
				);
		}
	}

	private class RequestChoice extends Thread
	{
		@Override
		public void run()
		{
			int choice	= controller.getChoice();
			if(choice != -1)
				writeLine(
					EGame.REPLY_CHOICE.toString() +
					((Integer)choice).toString()
				);
		}
	}
}
