package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.GameClient;

public class QuitListener implements ActionListener {
	
	private GameClient	controller	= null;
	
	public QuitListener(GameClient controller)
	{
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.toLobby();
	}

}
