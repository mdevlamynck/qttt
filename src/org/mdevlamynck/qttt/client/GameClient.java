package org.mdevlamynck.qttt.client;

import javax.swing.JFrame;

import org.mdevlamynck.qttt.client.gui.MainPanel;
import org.mdevlamynck.qttt.client.network.ChatHandler;
import org.mdevlamynck.qttt.client.network.GameSessionHandler;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;
import org.mdevlamynck.qttt.common.gamelogic.GameLogic;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;
import org.mdevlamynck.qttt.common.network.datastruct.Client;

public class GameClient extends JFrame {

	private static final long	serialVersionUID	= -5868967571057594004L;

	private MainPanel 			view 				= new MainPanel(this);
	
	private	GameLogic			gl					= null;
	
	private Turn				lastSelected		= new Turn();
	private	int					squareSelected		= 0;
	
	private Client				server				= new Client();

	private NetworkInputHandler	network				= new NetworkInputHandler(this, server);
	private GameSessionHandler	game				= new GameSessionHandler(this, server);
	private ChatHandler			chat				= new ChatHandler(this, server);
	
	private boolean				needTurn			= false;
	
	private GridSquare[][]		grid				= null;
	
	private	boolean				quit				= false;
	
	public GameClient()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		add(view);
		setSize(300, 150);

		resetGame();
		view.init();
		view.updateRender();
		
		view.enableSquares(false);

		network.start();
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
					e.printStackTrace();
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
					e.printStackTrace();
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
	
	public boolean getQuit()
	{
		return quit;
	}

	public void quit() {
		quit = true;
	}

}
