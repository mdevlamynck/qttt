package org.mdevlamynck.qttt.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.IHMBackend;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.messages.EServer;
import org.mdevlamynck.qttt.server.datastruct.Client;

public class GameSessionHandler extends Thread implements IHMBackend {
	
	private Client			p1;
	private Client			p2;
	
	private List<Client>	spectators		= new ArrayList<Client>();
	private List<Client>	allConnected	= new ArrayList<Client>();
	
	private GameLogic		gl				= null;
	
	public GameSessionHandler(Client p1, Client p2)
	{
		this.p1 = p1;
		this.p2 = p2;
		
		p1.session = this;
		p2.session = this;
		
		p1.out.println(EServer.GAME_START);
		p1.out.println("1");
		p2.out.println(EServer.GAME_START);
		p2.out.println("2");
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
			System.err.println(e.getMessage());
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
				
		player.out.println(EServer.GAME_REQUEST_TURN);
		turn	= new Turn();
		turn.fromString(player.in.nextLine());
		
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
		
		player.out.println(EServer.GAME_REQUEST_CHOICE);
		choose = player.in.nextInt();
		player.in.nextLine();
		
		return choose;
	}

	@Override
	public void gameFinished(GameResult result) {
		p1.out.println(EServer.GAME_FINISHED);
		p1.out.println(result.toString());
			
		p2.out.println(EServer.GAME_FINISHED);
		p2.out.println(result.toString());
	}

	@Override
	public void lastTurn(Turn turn) {
		if(turn == null)
			return;
		
		p1.out.println(EServer.GAME_OTHER_TURN);
		p1.out.println(turn.toString());
			
		p2.out.println(EServer.GAME_OTHER_TURN);
		p2.out.println(turn.toString());
	}

	@Override
	public void choice(int choice) {
		p1.out.println(EServer.GAME_OTHER_CHOICE);
		p1.out.println(((Integer)choice).toString());
			
		p2.out.println(EServer.GAME_OTHER_CHOICE);
		p2.out.println(((Integer)choice).toString());
	}

}
