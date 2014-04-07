package org.mdevlamynck.qttt.common.gamelogic;

import java.util.Arrays;
import java.util.Scanner;

public class Turn {
	
	public class Pos {
		public int col = -1;
		public int row = -1;
		
		@Override
		public String toString() {
			return col + " " + row;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + col;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pos other = (Pos) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (col != other.col)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

		private Turn getOuterType() {
			return Turn.this;
		}
		
		
	}
	
	public Pos[] pos = new Pos[2];
	
	public Turn() {
		for(int i = 0; i < pos.length; i++)
			pos[i] = new Pos();
	}

	@Override
	public String toString() {
		return pos[0].toString()+ " " + pos[1].toString();
	}
	
	public Turn fromString(String str)
	{
		Scanner scan = new Scanner(str);
		
		pos[0].col = scan.nextInt();
		pos[0].row = scan.nextInt();
		pos[1].col = scan.nextInt();
		pos[1].row = scan.nextInt();
		
		return this;
	}

}