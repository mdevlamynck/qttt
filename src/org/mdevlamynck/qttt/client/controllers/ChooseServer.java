package org.mdevlamynck.qttt.client.controllers;

import java.awt.Panel;

import org.mdevlamynck.qttt.client.MainFrame;
import org.mdevlamynck.qttt.client.views.ChoosePanel;

public class ChooseServer extends BasicController {

	private ChoosePanel view	= new ChoosePanel(this);
	
	public ChooseServer(MainFrame parent)
	{
		super(parent);
	}
	
	public void start()
	{
	}
	
	public void connect()
	{
		parent.setServer(view.getAddress(), view.getPort());
	}
	
	public void quit()
	{
		System.exit(0);
	}
	
	public Panel getPanel()
	{
		return view;
	}

}
