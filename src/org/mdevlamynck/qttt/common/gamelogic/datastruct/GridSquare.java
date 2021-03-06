package org.mdevlamynck.qttt.common.gamelogic.datastruct;

import java.util.Hashtable;


public class GridSquare {
	
	public enum EPlayer
	{
		P1,
		P2,
		
		None
	}
	
	public Integer	turnNumber		= -1;
	public EPlayer	player			= EPlayer.None;
	
	public Hashtable<
		Integer,
		EPlayer
	>				undefinedTurns	= new Hashtable<Integer, EPlayer>();
	
	public Hashtable<
		Integer,
		GridSquare
	>				linkedSquares	= new Hashtable<Integer, GridSquare>();

}
