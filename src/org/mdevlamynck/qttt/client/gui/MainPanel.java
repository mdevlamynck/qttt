package org.mdevlamynck.qttt.client.gui;

import java.awt.GridLayout;
import java.awt.Panel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.mdevlamynck.qttt.client.network.NetworkHandler;
import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.Turn;

public class MainPanel extends Panel {
	
	private JButton[][]		gridBtn			= null;
	private GridSquare[][]	gridData		= null;
	
	private	GameLogic		gl				= null;
	
	private Turn			lastSelected	= new Turn();
	private	int				squareSelected	= 0;
	
	private	Panel			board			= null;
	private	Panel			log				= null;
	
	private	JTextArea		logText			= new JTextArea();
	
	private NetworkHandler	network			= new NetworkHandler(this);
	
	private boolean			needTurn		= false;

	public MainPanel()
	{
		resetGame();	
		updateRender();
		
		enableSquares(false);

		network.start();
	}
	
	public void updateRender()
	{
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				String text = "";
				if		(gridData[col][row].player == EPlayer.NotYetPlayed)
					text += "-";
				else if	(gridData[col][row].player == EPlayer.P1)
					text += "o";
				else
					text += "x";
				
				if(gridData[col][row].turnNumber >= 0)
					text += gridData[col][row].turnNumber.toString();
				
				if(gridData[col][row].undefinedTurns.size() > 0)
				{
					text += "(";
					
					for(Integer it : gridData[col][row].undefinedTurns.keySet())
					{
						if		(gridData[col][row].undefinedTurns.get(it) == EPlayer.NotYetPlayed)
							text += "-";
						else if	(gridData[col][row].undefinedTurns.get(it) == EPlayer.P1)
							text += "o";
						else
							text += "x";
						
						if(it >= 0)
							text += it.toString();
					}
					text += ")";
				}

				gridBtn[col][row].setText(text);
			}
		}
	}
	
	public void selected(int col, int row)
	{
		if(squareSelected < 2)
		{
			synchronized (lastSelected) {
				lastSelected.pos[squareSelected].col = col;
				lastSelected.pos[squareSelected].row = row;
				squareSelected++;
				
				if		(needTurn && squareSelected == 2)
					lastSelected.notify();
				else if	(!needTurn && squareSelected == 1)
					lastSelected.notify();
			}
		}
	}
	
	public void resetGame()
	{
		gl			= new GameLogic(null);
		gridData	= gl.getGrid();
		gridBtn		= new JButton[gridData.length][gridData[0].length];
		
		removeAll();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		board		= new Panel();
		board.setLayout(new GridLayout(gridData.length, gridData[0].length));
		
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				gridBtn[col][row] = new JButton();
				gridBtn[col][row].addActionListener(new SquareListener(this, col, row));
				board.add(gridBtn[col][row]);
			}
		}
		
		add(board);
		
		log			= new Panel();
		log.setLayout(new GridLayout(1, 1));
		log.add(logText);
		add(log);
	}
	
	public Turn getTurn()
	{
		Turn turn = null;

		synchronized (lastSelected) {
			needTurn = true;
			
			enableSquares(true);
			
			while(squareSelected != 2) {
				try {
					lastSelected.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			squareSelected = 0;
			turn = lastSelected;
						
			enableSquares(false);
		}
		
		return turn;
	}
	
	public int getChoice()
	{
		Turn	turn	= null;
		int		choice	= -1;
		
		synchronized (lastSelected) {
			needTurn = false;
			
			turn = lastSelected;
			
			enableOnly(turn);
			
			while(squareSelected != 1) {
				try {
					lastSelected.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			squareSelected = 0;
			
			if		(turn.pos[1].equals(lastSelected.pos[0]))
				choice = 1;
			else if	(turn.pos[0].equals(lastSelected.pos[0]))
				choice = 0;
			
			enableSquares(false);
		}
		
		return choice;
	}
	
	public void addTurn(Turn t)
	{
		lastSelected = t;
		gl.playTurn(t);
		updateRender();
	}
	
	public void addChoice(int choice)
	{
		gl.resolvCycle(choice);
		updateRender();
	}
	
	public void addToLog(String  text)
	{
		synchronized (logText) {
			logText.setText(logText.getText()+ (logText.getText().equals("") ? "" : "\n") + text);
		}
	}
	
	public void clearLog()
	{
		synchronized (logText) {
			logText.setText(null);
		}
	}
	
	public void enableSquares(boolean enable)
	{
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				if(gridData[col][row].player != EPlayer.NotYetPlayed)
					gridBtn[col][row].setEnabled(false);
				else
					gridBtn[col][row].setEnabled(enable);
			}
		}
	}
	
	public void enableOnly(Turn t)
	{
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				if( (col == t.pos[0].col && row == t.pos[0].row) ||
					(col == t.pos[1].col && row == t.pos[1].row))
					gridBtn[col][row].setEnabled(true);
				else
					gridBtn[col][row].setEnabled(false);
			}
		}
	}

}
