package org.mdevlamynck.qttt.common.gamelogic;

public interface IHMBackend {
	
	public Turn	playTurn		(boolean isP1);
	public int	chooseCycle		(boolean isP1);
	public void	lastTurn		(Turn turn);
	public void	choice			(int choice);
	public void	gameFinished	(GameResult result);

}
