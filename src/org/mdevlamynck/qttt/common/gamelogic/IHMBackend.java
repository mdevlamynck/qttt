package org.mdevlamynck.qttt.common.gamelogic;

import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;

public interface IHMBackend {
	
	public Turn	playTurn		(boolean isP1);
	public int	chooseCycle		(boolean isP1);
	public void	lastTurn		(Turn turn);
	public void	choice			(int choice);
	public void	gameFinished	(GameResult result);

}
