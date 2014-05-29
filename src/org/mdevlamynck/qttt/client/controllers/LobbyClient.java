
package org.mdevlamynck.qttt.client.controllers;

import java.awt.Panel;
import java.io.IOException;
import java.util.List;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.client.network.LobbyHandler;
import org.mdevlamynck.qttt.client.network.NetworkInputHandler;
import org.mdevlamynck.qttt.client.views.LobbyPanel;
import org.mdevlamynck.qttt.common.network.datastruct.OtherEnd;

public class LobbyClient extends BasicController {

	private LobbyPanel			view	= new LobbyPanel(this);
	private NetworkInputHandler	network	= null;
	private LobbyHandler		lobby	= null;
	
	public LobbyClient(MainFrame parent)
	{
		super(parent);
	}
	
	public void start(NetworkInputHandler network)
	{
		this.network	= network;
		network.start();

		lobby			= new LobbyHandler(this, network.getServer());
		lobby.start();

		lobby.refreshSessions();
		lobby.refreshClients();
	}
	
	public Panel getPanel()
	{
		return view;
	}
	
	public void quit()
	{
		try {
			network.getServer().socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		network.interrupt();
		lobby.interrupt();
	}

	public void toGame()
	{
		parent.launchGame();
	}
	
	public void toChoose()
	{
		parent.lostConnection();
	}

	public void refresh()
	{
		if(view.isSessions())
		{
			lobby.refreshSessions();
			lobby.refreshClients();
		}
	}

	public void setSession(List<OtherEnd> sessions)
	{
		view.setSessions(sessions);
	}

	public void setClient(List<OtherEnd> clients)
	{
		view.setClients(clients);
	}

	public void request()
	{
		lobby.joinGame(view.getSelectedRow());
	}

	public void create()
	{
		lobby.createGame();
		lobby.refreshSessions();
	}

}
