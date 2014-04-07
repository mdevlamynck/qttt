package org.mdevlamynck.qttt.common.gamelogic;

import java.util.ArrayList;
import java.util.List;

import org.mdevlamynck.qttt.common.gamelogic.datastruct.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;

public class GameLogic {
	
	private enum EDirection
	{
		HORIZONTAL,
		VERTICAL,
		DIGONAL_SLASH,
		DIGONAL_ANTISLASH
	}
	
	private final int	SIZE		= 3;
	GridSquare[][]		grid		= new GridSquare[SIZE][SIZE];
	
	private	int			currentTurn	= 0;
	private boolean		isFinished	= false;

	private	GameResult	gr			= new GameResult();
	
	private IHMBackend	ihm			= null;
	
	private Turn		lastTurn	= null;
	
	/*************************************************************************/
	
	public GameLogic(IHMBackend ihm)
	{
		this.ihm = ihm;
		
		for(int col = 0; col < grid.length; col++)
		{
			for(int row = 0; row < grid[col].length; row++)
			{
				grid[col][row] = new GridSquare();
			}
		}
	}
	
	/*
	 * Play
	 */
	
	public void runGame()
	{
		while(!isGameFinished())
		{
			ihm.lastTurn(lastTurn);
			while(!playTurn(ihm.playTurn( (currentTurn % 2) == 0 )));

			if(checkCycles())
			{
				int i = -1;
				ihm.lastTurn(lastTurn);
				while(!resolvCycle(i = ihm.chooseCycle( (currentTurn % 2) == 0 )));
				ihm.choice(i);
			}
		}
		
		processScore();
		ihm.gameFinished( getResult() );
	}
	
	public boolean playTurn(Turn t)
	{
		if( t == null ||
			(t.pos[0].col == t.pos[1].col && t.pos[0].row == t.pos[1].row) ||
			!isAvailableSquare(t.pos[0].col, t.pos[0].row) ||
			!isAvailableSquare(t.pos[1].col, t.pos[1].row)
		)
			return false;

		grid[t.pos[0].col][t.pos[0].row].undefinedTurns.put(currentTurn, ((currentTurn % 2) == 0 ? EPlayer.P1 : EPlayer.P2));
		grid[t.pos[1].col][t.pos[1].row].undefinedTurns.put(currentTurn, ((currentTurn % 2) == 0 ? EPlayer.P1 : EPlayer.P2));
		grid[t.pos[0].col][t.pos[0].row].linkedSquares.put(currentTurn, grid[t.pos[1].col][t.pos[1].row]);
		grid[t.pos[1].col][t.pos[1].row].linkedSquares.put(currentTurn, grid[t.pos[0].col][t.pos[0].row]);
		
		lastTurn = t;

		currentTurn++;
		
		return true;
	}
	
	public boolean resolvCycle(int i)
	{
		if(i != 0 && i != 1)
			return false;
		
		resolvCycleRecurs(grid[lastTurn.pos[i].col][lastTurn.pos[i].row], currentTurn - 1);

		return true;
	}
	
	private void resolvCycleRecurs(GridSquare g, int turnToKeep)
	{
		if(!g.undefinedTurns.containsKey(turnToKeep))
			return;
		
		g.turnNumber = turnToKeep;
		g.player = g.undefinedTurns.get(turnToKeep);
		g.undefinedTurns.clear();

		for(Integer it : g.linkedSquares.keySet())
		{
			if(it != turnToKeep)
				resolvCycleRecurs(g.linkedSquares.get(it), it);
		}
		
		g.linkedSquares.clear();
	}
	
	public void stop()
	{
		isFinished = true;
	}
	
	/*
	 * Checks
	 */
	
	private boolean checkCycles()
	{
		List<GridSquare> visited = new ArrayList<GridSquare>(); 

		return checkCyclesRecurs(grid[lastTurn.pos[1].col][lastTurn.pos[1].row], visited, null);
	}
	
	private boolean checkCyclesRecurs(GridSquare g, List<GridSquare> visited, GridSquare from)
	{
		if(visited.contains(g))
			return true;
		
		visited.add(g);
		
		boolean found = false;
		
		for(Integer it : g.linkedSquares.keySet())
		{
			if(	g.linkedSquares.get(it) != from &&
				checkCyclesRecurs(g.linkedSquares.get(it), visited, g))
			{
				found = true;
				break;
			}
		}
		
		visited.add(g);
		return found;
	}
	
	private boolean isAvailableSquare(int column, int row)
	{
		if(!isValidCoordinate(column, row))
			return false;
		
		return (grid[column][row].player == EPlayer.NotYetPlayed);
	}
	
	private boolean isValidCoordinate(int column, int row)
	{
		return (column >= 0 && column < SIZE &&
				row >= 0 && row < SIZE);
	}
	
	/*
	 * Is Game Finished
	 */
	
	private boolean isGameFinished()
	{
		if(isFinished)
			return true;

		for(int i = 0; i < SIZE; i++)
		{
			if(checkConsecutive(EDirection.HORIZONTAL, i))
				isFinished = true;
			if(checkConsecutive(EDirection.VERTICAL, i))
				isFinished = true;
		}
		if(checkConsecutive(EDirection.DIGONAL_SLASH, -1))
			isFinished = true;
		if(checkConsecutive(EDirection.DIGONAL_ANTISLASH, -1))
			isFinished = true;
		
		if(checkFull())
			isFinished = true;
		
		return isFinished;
	}
	
	private boolean checkConsecutive(EDirection direction, int i)
	{
		int		col1			= 0;
		int		row1			= 0;
		int		col2			= 0;
		int		row2			= 0;
		
		boolean areAllTheSame	= true;
		int		oldestSquare	= -1;
		EPlayer	player			= EPlayer.NotYetPlayed;

		for(int j = 0; j < SIZE; j++)
		{
			if		(direction == EDirection.HORIZONTAL)
			{
				col1 = j;
				row1 = i;
				col2 = j+1;
				row2 = i;
			}
			else if	(direction == EDirection.VERTICAL)
			{
				col1 = i;
				row1 = j;
				col2 = i;
				row2 = j+1;
			}
			else if	(direction == EDirection.DIGONAL_SLASH)
			{
				col1 = j;
				row1 = SIZE - 1 - j;
				col2 = j+1;
				row2 = SIZE - 2 - j;
			}
			else
			{
				col1 = j;
				row1 = j;
				col2 = j+1;
				row2 = j+1;
			}
			if( (col2 >= 0 && col2 < SIZE && row2 >= 0 && row2 < SIZE) && (
				grid[col1][row1].player == EPlayer.NotYetPlayed ||
				grid[col1][row1].player != grid[col2][row2].player
			))
			{
				areAllTheSame = false;
				break;
			}
			else if (grid[col1][row1].turnNumber > oldestSquare)
			{
				oldestSquare	= grid[col1][row1].turnNumber;
				player			= grid[col1][row1].player;
			}
		}
		
		if(areAllTheSame)
		{
			if(player == EPlayer.P1)
				gr.oldestsP1.add(oldestSquare);
			else
				gr.oldestsP2.add(oldestSquare);
		}
		
		return areAllTheSame;
	}
	
	private boolean checkFull()
	{
		boolean isFull = true;
		
		for(int col = 0; col < grid.length; col++)
		{
			for(int row = 0; row < grid[col].length; row++)
			{
				if(grid[col][row].player == EPlayer.NotYetPlayed)
					isFull = false;
					
			}
		}
		
		return isFull;
	}
	
	private void processScore()
	{
		int		youngest	= Integer.MAX_VALUE;
		EPlayer	player		= EPlayer.NotYetPlayed;
		for(Integer oldest : gr.oldestsP1)
		{
			if(oldest < youngest)
			{
				youngest	= oldest;
				player		= EPlayer.P1;
			}
		}
		for(Integer oldest : gr.oldestsP2)
		{
			if(oldest < youngest)
			{
				youngest	= oldest;
				player		= EPlayer.P2;
			}
		}

		gr.scoreP1 = gr.oldestsP1.size() / 2f;
		gr.scoreP2 = gr.oldestsP2.size() / 2f;
		
		if		(player == EPlayer.P1)
			gr.scoreP1 += 1 / 2f;
		else if	(player == EPlayer.P2)
			gr.scoreP2 += 1 / 2f;
	}
	
	/*
	 * Getters 
	 */
	
	public GridSquare[][] getGrid()
	{
		return grid;
	}
	
	public GameResult getResult()
	{
		return gr;
	}
	
}
