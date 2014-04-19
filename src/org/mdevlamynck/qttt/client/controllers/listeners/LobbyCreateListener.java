package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;

public class LobbyCreateListener implements ActionListener {
	
	private LobbyClient	controller	= null;

	public LobbyCreateListener(LobbyClient controller)
	{
		this.controller	= controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controller.create();
	}

}
