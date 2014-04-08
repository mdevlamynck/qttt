package org.mdevlamynck.qttt.client.views;

import java.awt.Panel;

import javax.swing.JLabel;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;

public class LobbyPanel extends Panel {

	private static final long serialVersionUID = 989607544848325238L;
	
	private LobbyClient	controller	= null;
	
	public LobbyPanel(LobbyClient controller)
	{
		this.controller	= controller;

		add(new JLabel("Waiting for one other player"));
	}

}
