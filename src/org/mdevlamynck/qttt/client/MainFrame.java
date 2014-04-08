package org.mdevlamynck.qttt.client;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mdevlamynck.qttt.client.controllers.BasicController;
import org.mdevlamynck.qttt.client.controllers.ChooseServer;
import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;

public class MainFrame extends JFrame {
	
	private NetworkInputHandler	network	= new NetworkInputHandler(this);
	private	boolean				quit	= false;
	private JPanel				cards	= new JPanel(new CardLayout());
	private BasicController		current	= null;
	private	ChooseServer		choose	= new ChooseServer(this);
	private	LobbyClient			lobby	= new LobbyClient(this, network);
	private	GameClient			game	= new GameClient(this, network);
	
	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("QTTT");

		cards.add(choose.getPanel(), "choose");
		cards.add(lobby.getPanel(), "lobby");
		cards.add(game.getPanel(), "game");
		add(cards);
		
		setSize(400, 400);
		
		chooseServer();
	}
	
	public void chooseServer()
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "choose");
		current	= choose;
	}
	
	public void lobbyClient()
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "lobby");
		current	= lobby;
		lobby.start();
	}
	
	public void gameClient()
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "game");
		current = game;
		game.start();
	}

	public void setServer(String address, int port) {
		if(network.setServer(address, port))
			lobbyClient();
	}
	
	public void lostConnection()
	{
		current.quit();
	}

	public void quit() {
		quit = true;
	}

	public boolean getQuit() {
		return quit;
	}

}