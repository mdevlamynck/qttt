package org.mdevlamynck.qttt.client.gui;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.mdevlamynck.qttt.client.GameClient;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;

public class MainPanel extends Panel {
	
	private JButton[][]		gridBtn			= null;
	
	private GameClient		controller		= null;
	
	private	Panel			board			= new Panel();
	private	Panel			log				= new Panel();
	private	Panel			chat			= new Panel();
	
	private JTabbedPane		bottomPanel		= new JTabbedPane();
	
	private	JTextArea		logText			= new JTextArea();
	
	private	JTextArea		chatText		= new JTextArea();
	private JTextField		sendText		= new JTextField();

	public MainPanel(GameClient controller)
	{
		this.controller = controller;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		
		add(board);
		
		add(bottomPanel);
		
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
		chat.add(chatText);
		chat.add(sendText);
		chat.setName("Chat");
		bottomPanel.add(chat);
		
		sendText.addActionListener(new ChatListener(controller));

		log.setLayout(new BoxLayout(log, BoxLayout.Y_AXIS));
		log.add(logText);
		log.setName("Server Log");
		bottomPanel.add(log);
	}
	
	public void init()
	{
		GridSquare[][] gridData	= controller.getGrid();
		gridBtn					= new JButton[gridData.length][gridData[0].length];
		
		board.setLayout(new GridLayout(gridData.length, gridData[0].length));
		
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				gridBtn[col][row] = new JButton();
				gridBtn[col][row].addActionListener(new SquareListener(controller, col, row));
				board.add(gridBtn[col][row]);
			}
		}
	}
	
	public void updateRender()
	{
		GridSquare[][]	gridData	= controller.getGrid();
		
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
	
	public void addToLog(String  text, boolean isIn)
	{
		synchronized (logText) {
			logText.setText((logText.getText().equals("") ? "" : logText.getText() + "\n") +
							(isIn ? "Receive : " : "Send    : ") + text);
		}
	}
	
	public void clearLog()
	{
		synchronized (logText) {
			logText.setText(null);
		}
	}
	
	public void addToChat(String text, boolean isIn)
	{
		synchronized (chatText) {
			chatText.setText((chatText.getText().equals("") ? "" : chatText.getText() + "\n") +
							(isIn ? "Adv : " : "Me  : ") + text);	
		}
	}
	
	public void clearChat()
	{
		synchronized (chatText) {
			chatText.setText(null);
		}
	}
	
	public void enableSquares(boolean enable)
	{
		GridSquare[][] gridData	= controller.getGrid();
		for(int col = 0; col < gridBtn.length; col++)
		{
			for(int row = 0; row < gridBtn[col].length; row++)
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
		for(int col = 0; col < gridBtn.length; col++)
		{
			for(int row = 0; row < gridBtn[col].length; row++)
			{
				if( (col == t.pos[0].col && row == t.pos[0].row) ||
					(col == t.pos[1].col && row == t.pos[1].row))
					gridBtn[col][row].setEnabled(true);
				else
					gridBtn[col][row].setEnabled(false);
			}
		}
	}

	public String getSendText() {
		return sendText.getText();
	}

	public void clearSendText() {
		sendText.setText(null);
	}

}
