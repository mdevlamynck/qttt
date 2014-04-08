package org.mdevlamynck.qttt.common.network;

import java.util.LinkedList;

public class ConcurrentQueue<T> {

	private LinkedList<T>	queue	= new LinkedList<T>(); 
	
	public ConcurrentQueue()
	{
	}
	
	public void push(T data)
	{
		synchronized (queue) {
			boolean isEmpty = queue.isEmpty();
			queue.add(data);
			if(isEmpty)
				queue.notify();
		}
	}
	
	public T pop() throws InterruptedException
	{
		synchronized (queue) {
			while(queue.isEmpty())
				queue.wait();
			
			return queue.remove();
		}
	}

}
