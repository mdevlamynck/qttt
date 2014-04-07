package org.mdevlamynck.qttt.common.network;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
	
	Queue<String>	messages	= new LinkedList<String>();
	
	public MessageQueue()
	{
	}
	
	public void push(String messg)
	{
		synchronized (messages) {
			boolean isEmpty = messages.isEmpty();
			messages.add(messg);
			if(isEmpty)
				messages.notify();
		}
	}
	
	public String pop()
	{
		synchronized (messages) {
			while(messages.isEmpty())
				try {
					messages.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return messages.remove();
		}
	}

}
