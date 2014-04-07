package org.mdevlamynck.qttt.common.gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameResult {

	public float			scoreP1		= 0;
	public float			scoreP2		= 0;
	
	public List<Integer>	oldestsP1	= new ArrayList<Integer>();
	public List<Integer>	oldestsP2	= new ArrayList<Integer>();

	@Override
	public String toString() {
		return scoreP1 + " " + scoreP2;
	}
	
	public void fromString(String str) {
		Scanner scan = new Scanner(str);
		
		scoreP1	= scan.nextInt();
		scoreP2	= scan.nextInt();
	}

}
