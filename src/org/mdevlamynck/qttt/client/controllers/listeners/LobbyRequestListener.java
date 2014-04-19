package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;

public class LobbyRequestListener implements ActionListener {
	
	private LobbyClient	controller	= null;

	public LobbyRequestListener(LobbyClient controller)
	{
		this.controller	= controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		controller.request();
	}

}
