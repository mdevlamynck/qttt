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
				
				if		( line.equals(EGame.START.toString())			)
					controller.setPlayer	( Integer.parseInt(readLine()) == 1 ? EPlayer.P1 : EPlayer.P2	);
			
				else if	( line.equals(EGame.REQUEST_TURN.toString())	)
				{
					Turn turn	= controller.getTurn();
					if(turn != null)
						writeLine	( turn.toString()				);
				}

				else if	( line.equals(EGame.REQUEST_CHOICE.toString())	)
				{
					int choice	= controller.getChoice();
					if(choice != -1)
						writeLine	( ((Integer)choice).toString()	);
				}
				
				else if	( line.equals(EGame.OTHER_TURN.toString())		)
					controller.addTurn		( new Turn().fromString(readLine()) );
				
				else if	( line.equals(EGame.OTHER_CHOICE.toString())	)
					controller.addChoice	( Integer.parseInt(readLine()) 		);

				else if	( line.equals(EGame.FINISHED.toString())		)
				{
					readLine();
					break;
				}

				else if	( line.equals(EGame.INTERRUPTED.toString())		)
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

		System.out.println(line);

		controller.addToLog(line, true);
		return line;
	}
	
	private void writeLine(String line)
	{
		server.out.println(EPrefixes.GAME.toString() + line);
		controller.addToLog(line, false);
	}

	@Override
	public void addMessage(OtherEndMessage mess) {
		server.game.push(mess.message);
	}
	
}
