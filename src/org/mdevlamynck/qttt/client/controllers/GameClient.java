
package org.mdevlamynck.qttt.client.controllers;

import java.awt.Panel;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.client.network.ChatHandler;
import org.mdevlamynck.qttt.client.network.GameSessionHandler;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;
import org.mdevlamynck.qttt.client.views.GamePanel;
import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;

public class GameClient extends BasicController {
	
	private GamePanel 			view 				= new GamePanel(this);
	
	private	GameLogic			gl					= null;
	
	private Turn				lastSelected		= new Turn();
	private	int					squareSelected		= 0;

	private NetworkInputHandler	network				= null;
	private GameSessionHandler	game				= null;
	private ChatHandler			chat				= null;
	
	private boolean				needTurn			= false;
	
	private GridSquare[][]		grid				= null;
	
	public GameClient(MainFrame parent)
	{
		super(parent);

		resetGame();
		view.init();
		view.updateRender();
		
		view.enableSquares(false);
	}
	
	public void start(NetworkInputHandler network)
	{
		this.network	= network;
		
		this.game		= new GameSessionHandler(this, network.getServer());
		this.chat		= new ChatHandler(this, network.getServer());
		
		game.start();
		chat.start();
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

	public void sendChat() {
		String text = view.getSendText();
		chat.sendChatMessg(text);
		view.clearSendText();
		view.addToChat(text, false);
	}

	public Turn getTurn()
	{
		Turn turn = null;

		synchronized (lastSelected) {
			needTurn = true;
			
			view.enableSquares(true);
			
			while(squareSelected != 2) {
				try {
					lastSelected.wait();
				} catch (InterruptedException e) {
					return null;
				}
			}
			squareSelected = 0;
			turn = lastSelected;
						
			view.enableSquares(false);
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
			
			view.enableOnly(turn);
			
			while(squareSelected != 1) {
				try {
					lastSelected.wait();
				} catch (InterruptedException e) {
					return -1;
				}
			}
			squareSelected = 0;
			
			if		(turn.pos[1].equals(lastSelected.pos[0]))
				choice = 1;
			else if	(turn.pos[0].equals(lastSelected.pos[0]))
				choice = 0;
			
			view.enableSquares(false);
		}
		
		return choice;
	}
	
	public void addTurn(Turn t)
	{
		lastSelected = t;
		gl.playTurn(t);
		view.updateRender();
	}
	
	public void addChoice(int choice)
	{
		gl.resolvCycle(choice);
		view.updateRender();
	}
	
	public void resetGame()
	{
		gl		= new GameLogic(null);
		grid	= gl.getGrid();
	}
	
	public void addToLog(String text, boolean isIn)
	{
		view.addToLog(text, isIn);
	}
	
	public void clearLog()
	{
		view.clearLog();
	}

	public void addToChat(String text, boolean isIn)
	{
		view.addToChat(text, isIn);
	}
	
	public void clearChat()
	{
		view.clearChat();
	}
	
	public GridSquare[][] getGrid()
	{
		return grid;
	}

	public void quit()
	{
		network.interrupt();
		game.interrupt();
		chat.interrupt();
	}
	
	public Panel getPanel()
	{
		return view;
	}

}
