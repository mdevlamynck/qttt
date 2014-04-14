package org.mdevlamynck.qttt.client.views;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;

public class LobbyPanel extends Panel {

	private static final long serialVersionUID = 989607544848325238L;
	
	private LobbyClient				controller		= null;

	private JTable					sessionsTable	= new JTable();
	private JScrollPane				sessionsPane	= new JScrollPane(sessionsTable);
	private DefaultTableModel		sessionsData	= new DefaultTableModel();
	
	public LobbyPanel(LobbyClient controller)
	{
		this.controller	= controller;
		
		setLayout(new BorderLayout());
		
		sessionsTable.setFillsViewportHeight(true);
		sessionsTable.setModel(sessionsData);
		add(sessionsPane);
		sessionsData.addColumn("Game Sessions");
		
		sessionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sessionsTable.setPreferredSize(null);
	}
	
	public void setSessions(List<String> sessions)
	{
		synchronized (sessions)
		{
			while(sessionsData.getRowCount() > 0)
				sessionsData.removeRow(0);
			
			for(String s : sessions)
			{
				Object[] obj = {s};
				sessionsData.addRow(obj);
			}		
		}
	}

}
