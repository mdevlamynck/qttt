package org.mdevlamynck.qttt.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mdevlamynck.qttt.client.GameClient;

public class SquareListener implements ActionListener {
	
	private int			col			= -1;
	private int			row			= -1;
	private GameClient	controller	= null;
	
	public SquareListener(GameClient controller, int col, int row) {
		this.col		= col;
		this.row		= row;
		this.controller	= controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.selected(col, row);
	}

}
