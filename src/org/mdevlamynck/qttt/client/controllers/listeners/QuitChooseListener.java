package org.mdevlamynck.qttt.client.controllers.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.controllers.ChooseServer;

public class QuitChooseListener implements ActionListener {
	
	private ChooseServer	controller	= null;
	
	public QuitChooseListener(ChooseServer controller)
	{
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.quit();
	}

}
