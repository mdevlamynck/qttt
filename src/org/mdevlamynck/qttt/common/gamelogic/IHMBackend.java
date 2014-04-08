package org.mdevlamynck.qttt.common.gamelogic;

import org.mdevlamynck.qttt.common.gamelogic.Exceptions.StopGameException;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;

public interface IHMBackend {
	
	public Turn	playTurn		(boolean isP1) throws StopGameException;
	public int	chooseCycle		(boolean isP1) throws StopGameException;
	public void	lastTurn		(Turn turn);
	public void	choice			(int choice);
	public void	gameFinished	(GameResult result);
	public void	gameInterrupted	();

}
