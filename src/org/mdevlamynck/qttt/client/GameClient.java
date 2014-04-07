package org.mdevlamynck.qttt.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

import org.mdevlamynck.qttt.client.gui.MainPanel;
import org.mdevlamynck.qttt.common.messages.EClient;
import org.mdevlamynck.qttt.common.messages.EServer;

public class GameClient extends JFrame {

	private static final long serialVersionUID = -5868967571057594004L;

	private MainPanel panel = new MainPanel();
	
	public GameClient()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		add(panel);
		setSize(300, 150);
	}
	
	/*
	public void testCommunication()
	{
		try
		{
			Socket			server	= new Socket("localhost", 42042);

			BufferedReader	in		= new BufferedReader(new InputStreamReader(server.getInputStream()));
			PrintWriter		out		= new PrintWriter(server.getOutputStream(), true);
			
			out.println(EClient.CLIENT_CONNECTION);
			if(in.readLine().equals(EServer.CLIENT_CONNECTED.toString()))
				System.out.println("Connection Success");
			else
				System.out.println("Connection Failed");
			
			Thread.sleep(5000);
			
			out.println(EClient.SERVER_STOP);
			if(in.readLine().equals(EServer.SERVER_STOPPING.toString()))
				System.out.println("Server Stop Success");
			else
				System.out.println("Server Stop Failed");
			
			server.close();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	*/

}
