package org.mdevlamynck.qttt.client.controllers;

import org.mdevlamynck.qttt.client.MainFrame;

public class BasicController {
	
	private MainFrame	parent	= null;
	
	public BasicController(MainFrame parent)
	{
		this.parent	= parent;
	}
	
	public boolean getQuit()
	{
		return parent.getQuit();
	}

	public void quit() {
		parent.quit();
	}

}
