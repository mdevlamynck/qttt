package org.mdevlamynck.qttt.client.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.mdevlamynck.qttt.client.controllers.LobbyClient;
import org.mdevlamynck.qttt.client.controllers.listeners.LobbyCreateListener;
import org.mdevlamynck.qttt.client.controllers.listeners.LobbyQuitListener;
import org.mdevlamynck.qttt.client.controllers.listeners.LobbyRefreshListener;
import org.mdevlamynck.qttt.client.controllers.listeners.LobbyRequestListener;

public class LobbyPanel extends Panel {

	private static final long serialVersionUID = 989607544848325238L;

	private LobbyClient				controller		= null;

	private JTabbedPane				tablesPane		= new JTabbedPane();
	private Panel					bottomBtnPanel	= new Panel();

	private JTable					sessionsTable	= new JTable();
	private JScrollPane				sessionsPane	= new JScrollPane(sessionsTable);
	private DefaultTableModel		sessionsData	= new DefaultTableModel();

	private JTable					clientsTable	= new JTable();
	private JScrollPane				clientsPane		= new JScrollPane(clientsTable);
	private DefaultTableModel		clientsData		= new DefaultTableModel();

	private JButton					quitBtn			= new JButton("Quit");
	private JButton					refreshBtn		= new JButton("Refresh");
	private JButton					requestBtn		= new JButton("Send Request");
	private JButton					createBtn		= new JButton("Create Game");

	public LobbyPanel(LobbyClient controller)
	{
		this.controller	= controller;

		setLayout(new BorderLayout());

		add(tablesPane, BorderLayout.CENTER);

		sessionsTable.setFillsViewportHeight(true);
		sessionsTable.setModel(sessionsData);

		sessionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sessionsTable.setPreferredSize(null);
		sessionsData.addColumn("Game Sessions");

		sessionsPane.setName("Game Sessions");
		tablesPane.add(sessionsPane);

		clientsTable.setFillsViewportHeight(true);
		clientsTable.setModel(clientsData);

		clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientsTable.setPreferredSize(null);
		clientsData.addColumn("Other Players");

		clientsPane.setName("Other Players");
		tablesPane.add(clientsPane);

		add(bottomBtnPanel, BorderLayout.SOUTH);
		bottomBtnPanel.setLayout(new GridLayout(2, 2));
		bottomBtnPanel.add(requestBtn);
		bottomBtnPanel.add(createBtn);
		bottomBtnPanel.add(quitBtn);
		bottomBtnPanel.add(refreshBtn);

		requestBtn	.addActionListener( new LobbyRequestListener(controller)	);
		createBtn	.addActionListener( new LobbyCreateListener(controller)		);
		quitBtn		.addActionListener( new LobbyQuitListener(controller)		);
		refreshBtn	.addActionListener( new LobbyRefreshListener(controller)	);
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

	public void setClients(List<String> clients)
	{
		synchronized (clients)
		{
			while(clientsData.getRowCount() > 0)
				clientsData.removeRow(0);

			for(String c : clients)
			{
				Object[] obj = {c};
				clientsData.addRow(obj);
			}
		}
	}

	public boolean isSessions()
	{
		return true;
	}

}
