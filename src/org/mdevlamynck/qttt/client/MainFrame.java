package org.mdevlamynck.qttt.client;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mdevlamynck.qttt.client.controllers.ChooseServer;
import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 3007658074038398950L;
	
	private NetworkInputHandler	network			= null;
	private JPanel				cards			= new JPanel(new CardLayout());
	private	ChooseServer		choose			= new ChooseServer(this);
	private	LobbyClient			lobby			= new LobbyClient(this);
	private	GameClient			game			= new GameClient(this);
	
	private boolean				isGameLaunched	= false;
	
	public enum EScreen
	{
		CHOOSE,
		LOBBY,
		GAME
	}
	
	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("QTTT");

		cards.add( choose.getPanel(),	EScreen.CHOOSE.toString()	);
		cards.add( lobby.getPanel(),	EScreen.LOBBY.toString()	);
		cards.add( game.getPanel(),		EScreen.GAME.toString()		);
		add(cards);
		
		setSize(450, 450);

		switchTo(EScreen.CHOOSE);
	}
	
	public void switchTo(EScreen screen)
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, screen.toString());
	}

	public void connectToServer(String name, String address, int port) {
		network	= new NetworkInputHandler(this);
		if(network.setServer(name, address, port))
		{
			switchTo(EScreen.LOBBY);
			lobby.start(network);
		}
	}

	public void launchGame()
	{
		isGameLaunched = true;
		switchTo(EScreen.GAME);
		game.start(network);
	}

	public void quitGame()
	{
		isGameLaunched = false;
		switchTo(EScreen.LOBBY);
		game.quit();
	}
	
	public void lostConnection()
	{
		lobby.quit();
		if(isGameLaunched)
		{
			isGameLaunched = false;
			game.quit();
		}

		switchTo(EScreen.CHOOSE);
	}

}
