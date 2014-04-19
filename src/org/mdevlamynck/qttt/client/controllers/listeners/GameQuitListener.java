package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.GameClient;

public class GameQuitListener implements ActionListener {
	
	private GameClient	controller	= null;
	
	public GameQuitListener(GameClient controller)
	{
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.toLobby();
	}

}
