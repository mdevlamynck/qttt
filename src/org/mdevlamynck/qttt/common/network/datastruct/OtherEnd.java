package org.mdevlamynck.qttt.common.network.datastruct;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class OtherEnd {

	public UUID			id		= null;
	public Socket		socket	= null;
	public Scanner		in		= null;
	public PrintWriter	out		= null;
	public String		name	= "nonickname";

}