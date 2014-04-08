package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.GameClient;
import org.mdevlamynck.qttt.common.gamelogic.GameLogic;

public class ChatListener implements ActionListener {
	
	private GameClient	controller	= null;

	public ChatListener(GameClient controller)
	{
		this.controller	= controller;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		controller.sendChat();
	}

}
