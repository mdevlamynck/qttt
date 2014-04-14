package org.mdevlamynck.qttt.client.views;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.client.controllers.listeners.ChatListener;
import org.mdevlamynck.qttt.client.controllers.listeners.QuitListener;
import org.mdevlamynck.qttt.client.controllers.listeners.SquareListener;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.GridSquare.EPlayer;
import org.mdevlamynck.qttt.common.gamelogic.datastruct.Turn;

public class GamePanel extends Panel {

	private static final long serialVersionUID = 3911793017047934501L;

	private JButton[][]		gridBtn			= null;
	
	private GameClient		controller		= null;
	
	private	Panel			boardPanel		= new Panel();
	private	Panel			logPanel		= new Panel();
	private	Panel			chatPanel		= new Panel();
	private	Panel			chatSendPanel	= new Panel();
	
	private JTabbedPane		bottomPanel		= new JTabbedPane();
	
	private	JTextArea		logText			= new JTextArea();
	
	private	JTextArea		chatText		= new JTextArea();
	private JTextField		sendText		= new JTextField();

	private JButton			sendBtn			= new JButton("Send");
	private JButton			quitBtn			= new JButton("Quit");

	public GamePanel(GameClient controller)
	{
		this.controller = controller;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		
		add(boardPanel);
		
		add(bottomPanel);
		
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.add		(chatText);
		chatPanel.add		(chatSendPanel);

		chatSendPanel.setLayout(new BoxLayout(chatSendPanel, BoxLayout.X_AXIS));
		chatSendPanel.add	(sendText);
		chatSendPanel.add	(sendBtn);

		chatPanel.setName	("Chat");
		bottomPanel.add		(chatPanel);
		
		ActionListener	chatSendListener	= new ChatListener(controller);
		sendText.addActionListener	(chatSendListener);
		sendBtn.addActionListener	(chatSendListener);

		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
		logPanel.add		(logText);
		logPanel.setName	("Server Log");
		bottomPanel.add		(logPanel);
		
		add(quitBtn);
		quitBtn.addActionListener(new QuitListener(controller));
	}
	
	public void init()
	{
		GridSquare[][] gridData	= controller.getGrid();
		gridBtn					= new JButton[gridData.length][gridData[0].length];
		
		boardPanel.setLayout(new GridLayout(gridData.length, gridData[0].length));
		
		for(int col = 0; col < gridData.length; col++)
		{
			for(int row = 0; row < gridData[col].length; row++)
			{
				gridBtn[col][row] = new JButton();
				gridBtn[col][row].addActionListener(new SquareListener(controller, col, row));
				boardPanel.add(gridBtn[col][row]);
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
				if		(gridData[col][row].player == EPlayer.None)
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
						if		(gridData[col][row].undefinedTurns.get(it) == EPlayer.None)
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
				if(gridData[col][row].player != EPlayer.None)
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
