package org.mdevlamynck.qttt.client.controllers;

import java.awt.Panel;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.client.network.LobbyHandler;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;
import org.mdevlamynck.qttt.client.views.LobbyPanel;

public class LobbyClient extends BasicController {

	private MainFrame			parent	= null;
	private LobbyPanel			view	= new LobbyPanel(this);
	private NetworkInputHandler	network	= null;
	private LobbyHandler		lobby	= null;
	
	public LobbyClient(MainFrame parent, NetworkInputHandler network)
	{
		super(parent);

		this.parent		= parent;
		this.network	= network;
		
		lobby			= new LobbyHandler(this, network.getServer());
	}
	
	public void start()
	{
		network.start();
		lobby.start();
	}
	
	public Panel getPanel()
	{
		return view;
	}
	
	public void quit()
	{
		network.interrupt();
		lobby.interrupt();

		parent.chooseServer();
	}
	
	public void toConnection() {
		parent.chooseServer();
	}

	public void toGame() {
		parent.gameClient();
	}

}
