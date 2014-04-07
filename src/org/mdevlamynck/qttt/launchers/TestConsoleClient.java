package org.mdevlamynck.qttt.launchers;

import java.util.Scanner;

import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.GameResult;
import org.mdevlamynck.qttt.common.gamelogic.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.IHMBackend;
import org.mdevlamynck.qttt.common.gamelogic.Turn;
import org.mdevlamynck.qttt.common.gamelogic.Turn.Pos;

public class TestConsoleClient implements IHMBackend{
	
	private GameLogic gl = new GameLogic(this);
	
	Scanner	scan	= new Scanner(System.in);
	
	public TestConsoleClient()
	{
		System.out.println("Run");
		gl.runGame();
	}

	@Override
	public Turn playTurn(boolean isP1) {
		render();
		System.out.println("Play");
		
		Turn	t		= new Turn();
		
		t.pos[0].col	= scan.nextInt();
		t.pos[0].row	= scan.nextInt();
		t.pos[1].col	= scan.nextInt();
		t.pos[1].row	= scan.nextInt();
		return t;
	}

	@Override
	public int chooseCycle(boolean isP1) {
		render();
		System.out.println("Choose");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void gameFinished(GameResult result) {
		render();
		System.out.println("P1 : " + result.scoreP1);
		System.out.println("P2 : " + result.scoreP2);
		System.out.println("Stop");
		gl.stop();
	}

	@Override
	public void lastTurn(Turn turn) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void choice(int choice) {
		// TODO Auto-generated method stub	
	}
	
	private void render()
	{
		System.out.println("Render");
		GridSquare[][] grid = gl.getGrid();
		
		for(int col = 0; col < grid.length; col++)
		{
			for(int row = 0; row < grid[col].length; row++)
			{
				if		(grid[row][col].player == EPlayer.NotYetPlayed)
					System.out.print("-");
				else if	(grid[row][col].player == EPlayer.P1)
					System.out.print("o");
				else
					System.out.print("x");
				
				if(grid[row][col].turnNumber >= 0)
					System.out.print(grid[row][col].turnNumber.toString());
				
				System.out.print("(");
				
				for(Integer it : grid[row][col].undefinedTurns.keySet())
				{
					if		(grid[row][col].undefinedTurns.get(it) == EPlayer.NotYetPlayed)
						System.out.print("-");
					else if	(grid[row][col].undefinedTurns.get(it) == EPlayer.P1)
						System.out.print("o");
					else
						System.out.print("x");
					
					if(it >= 0)
						System.out.print(it.toString());
				}
				System.out.print(")");
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		new TestConsoleClient();
	}

}
