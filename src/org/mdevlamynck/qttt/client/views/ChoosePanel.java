package org.mdevlamynck.qttt.client.views;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.mdevlamynck.qttt.client.controllers.ChooseServer;
import org.mdevlamynck.qttt.client.controllers.listeners.ConnectChooseListener;
import org.mdevlamynck.qttt.client.controllers.listeners.QuitChooseListener;

public class ChoosePanel extends Panel {

	private static final long serialVersionUID = -2583801359782860518L;

	private JTextField		nameField	= new JTextField("nonickname");
	private JTextField		serverField	= new JTextField("localhost");
	private JTextField		portField	= new JTextField("42042");
	private JButton			quitBtn		= new JButton("Quit");
	private JButton			connectBtn	= new JButton("Connect");
	
	public ChoosePanel(ChooseServer controller)
	{
		setLayout(new GridLayout(4, 2, 5, 5));
		
		add(new JLabel("Nick Name : "));
		add(nameField);
		add(new JLabel("Server : "));
		add(serverField);
		add(new JLabel("Port : "));
		add(portField);
		add(quitBtn);
		add(connectBtn);
		
		quitBtn.addActionListener(new QuitChooseListener(controller));
		connectBtn.addActionListener(new ConnectChooseListener(controller));
	}
	
	public String getName()
	{
		return nameField.getText();
	}
	
	public String getAddress()
	{
		return serverField.getText();
	}

	public int getPort()
	{
		return Integer.parseInt(portField.getText());
	}

}
