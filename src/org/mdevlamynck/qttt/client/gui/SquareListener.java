package org.mdevlamynck.qttt.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SquareListener implements ActionListener {
	
	private int			col		= -1;
	private int			row		= -1;
	private MainPanel	panel	= null;
	
	public SquareListener(MainPanel panel, int col, int row) {
		this.col	= col;
		this.row	= row;
		this.panel	= panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panel.selected(col, row);
	}

}
