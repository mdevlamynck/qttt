package org.mdevlamynck.qttt.client.controllers;

import org.mdevlamynck.qttt.client.MainFrame;

public abstract class BasicController {
	
	protected MainFrame	parent	= null;
	
	public BasicController(MainFrame parent)
	{
		this.parent	= parent;
	}

	abstract public void quit();
	

}
