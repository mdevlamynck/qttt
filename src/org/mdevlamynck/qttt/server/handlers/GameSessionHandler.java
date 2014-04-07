package org.mdevlamynck.qttt.server.handlers;

import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.IHMBackend;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.messages.EServer;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class GameSessionHandler extends Thread implements IHMBackend {
	
	private Client			p1;
	private Client			p2;
	
	private GameLogic		gl				= null;
	
	private ChatHandler		chatFromP1		= null;
	private ChatHandler		chatFromP2		= null;
	
	public GameSessionHandler(Client p1, Client p2)
	{
		this.p1 = p1;
		this.p2 = p2;
		
		p1.session = this;
		p2.session = this;
		
		writeLine(p1, EServer.GAME_START.toString());
		writeLine(p1, "1");
		writeLine(p2, EServer.GAME_START.toString());
		writeLine(p2, "2");
		
		chatFromP1 = new ChatHandler(p1, p2);
		chatFromP2 = new ChatHandler(p2, p1);
		
		chatFromP1.start();
		chatFromP2.start();
	}
	
	@Override
	public void run()
	{
		try
		{
			gl = new GameLogic(this);
			gl.runGame();
		}
		catch(Exception e)
		{
			System.err.println("GSH-R " + e.getMessage());
		}
	}

	@Override
	public Turn playTurn(boolean isP1) {
		Client	player	= null;
		Turn	turn	= null;
		
		if(isP1)
			player = p1;
		else
			player = p2;
				
		writeLine(player, EServer.GAME_REQUEST_TURN.toString());
		turn	= new Turn();
		turn.fromString(player.game.pop());
		
		return turn;
	}

	@Override
	public int chooseCycle(boolean isP1) {
		Client	player	= null;
		int		choose	= -1;
		
		if(isP1)
			player = p1;
		else
			player = p2;
		
		writeLine(player, EServer.GAME_REQUEST_CHOICE.toString());
		choose = Integer.parseInt(player.game.pop());
		
		return choose;
	}

	@Override
	public void gameFinished(GameResult result) {
		writeLine(p1, EServer.GAME_FINISHED.toString());
		writeLine(p1, result.toString());
			
		writeLine(p2, EServer.GAME_FINISHED.toString());
		writeLine(p2, result.toString());
	}

	@Override
	public void lastTurn(Turn turn) {
		if(turn == null)
			return;
		
		writeLine(p1, EServer.GAME_OTHER_TURN.toString());
		writeLine(p1, turn.toString());
			
		writeLine(p2, EServer.GAME_OTHER_TURN.toString());
		writeLine(p2, turn.toString());
	}

	@Override
	public void choice(int choice) {
		writeLine(p1, EServer.GAME_OTHER_CHOICE.toString());
		writeLine(p1, ((Integer)choice).toString());
			
		writeLine(p2, EServer.GAME_OTHER_CHOICE.toString());
		writeLine(p2, ((Integer)choice).toString());
	}
	
	private void writeLine(Client client, String line)
	{
		client.out.println(EServer.GAME_PREFIX.toString() + line);
	}

}
