package org.mdevlamynck.qttt.server.handlers;

import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.IHMBackend;
import org.mdevlamynck.qttt.common.gamelogic.Exceptions.StopGameException;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.network.BasicHandler;
import org.mdevlamynck.qttt.common.network.ConcurrentQueue;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEndMessage;
import org.mdevlamynck.qttt.common.network.messages.EGame;
import org.mdevlamynck.qttt.common.network.messages.EPrefixes;
import org.mdevlamynck.qttt.server.datastructs.Client;

public class GameSessionHandler extends BasicHandler implements IHMBackend {
	
	private Client					p1;
	private Client					p2;
	
	private GameLogic				gl		= null;
	
	private ChatHandler				chat	= null;
	
	private ConcurrentQueue<String> messP1	= new ConcurrentQueue<String>();
	private ConcurrentQueue<String> messP2	= new ConcurrentQueue<String>();
	
	public GameSessionHandler(Client p1, Client p2)
	{
		handlerPrefix	= EPrefixes.GAME;

		this.p1 		= p1;
		this.p2 		= p2;
		
		p1.gameHandler	= this;
		p2.gameHandler	= this;
		
		writeLine(p1, EGame.START.toString());
		writeLine(p1, "1");
		writeLine(p2, EGame.START.toString());
		writeLine(p2, "2");
		
		chat = new ChatHandler(p1, p2);
		
		chat.start();
	}
	
	@Override
	public void run()
	{
		gl = new GameLogic(this);
		gl.runGame();
		
		chat.interrupt();

		try
		{
			chat.join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Turn playTurn(boolean isP1) throws StopGameException {
		Client					player	= null;
		ConcurrentQueue<String>	mess	= null;
		Turn					turn	= null;
		
		if(isP1)
		{
			player	= p1;
			mess	= messP1;
		}
		else
		{
			player	= p2;
			mess	= messP2;
		}
				
		writeLine(player, EGame.REQUEST_TURN.toString());
		turn	= new Turn();
		
		try
		{
			turn.fromString(mess.pop());
		}
		catch(InterruptedException e)
		{
			throw new StopGameException();
		}
		
		return turn;
	}

	@Override
	public int chooseCycle(boolean isP1) throws StopGameException {
		Client					player	= null;
		ConcurrentQueue<String>	mess	= null;
		int						choose	= -1;
		
		if(isP1)
		{
			player	= p1;
			mess	= messP1;
		}
		else
		{
			player	= p2;
			mess	= messP2;
		}
		
		writeLine(player, EGame.REQUEST_CHOICE.toString());
		
		try
		{
			choose = Integer.parseInt(mess.pop());
		}
		catch(InterruptedException e)
		{
			throw new StopGameException();
		}
		
		return choose;
	}

	@Override
	public void gameFinished(GameResult result) {
		writeLine(p1, EGame.FINISHED.toString());
		writeLine(p1, result.toString());
			
		writeLine(p2, EGame.FINISHED.toString());
		writeLine(p2, result.toString());
	}

	@Override
	public void lastTurn(Turn turn) {
		if(turn == null)
			return;
		
		writeLine(p1, EGame.OTHER_TURN.toString());
		writeLine(p1, turn.toString());
			
		writeLine(p2, EGame.OTHER_TURN.toString());
		writeLine(p2, turn.toString());
	}

	@Override
	public void choice(int choice) {
		writeLine(p1, EGame.OTHER_CHOICE.toString());
		writeLine(p1, ((Integer)choice).toString());
			
		writeLine(p2, EGame.OTHER_CHOICE.toString());
		writeLine(p2, ((Integer)choice).toString());
	}
	
	@Override
	public void gameInterrupted()
	{
		if(!p1.socket.isClosed())
			writeLine(p1, EGame.INTERRUPTED.toString());
		
		if(!p2.socket.isClosed())
			writeLine(p2, EGame.INTERRUPTED.toString());
	}
	
	@Override
	public void addMessage(OtherEndMessage mess)
	{
		if		(mess.client.equals(p1))
			messP1.push(mess.message);
		else if	(mess.client.equals(p2))
			messP2.push(mess.message);
	}

}
