package org.mdevlamynck.qttt.common.gamelogic.Exceptions;

public class StopGameException extends Exception {

	private static final long serialVersionUID = 4076504009301212219L;

	public StopGameException() { super(); }
	public StopGameException(String message) { super(message); }
	public StopGameException(String message, Throwable cause) { super(message, cause); }
	public StopGameException(Throwable cause) { super(cause); }
	
}