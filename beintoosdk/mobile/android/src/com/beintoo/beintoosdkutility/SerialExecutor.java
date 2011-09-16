package com.beintoo.beintoosdkutility;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialExecutor implements Executor {
	  final Queue<Runnable> tasks = new LinkedList<Runnable>();
	  final Executor executor;
	  Runnable active;
	  private static SerialExecutor single;
	   
	  public synchronized static SerialExecutor getInstance() {
	      if(single==null) {
	          single = new SerialExecutor();
	      }
	       
	      return single;
	  }
	
	  private SerialExecutor() {
		  	ExecutorService executor = Executors.newFixedThreadPool(3);
		    this.executor = executor;
	  }
	  public synchronized void execute(final Runnable r) {
	    tasks.offer(new Runnable() {
	      public void run() {
	        try {
	          r.run();
	        } finally {
	          scheduleNext();
	        }
	      }
	    });
	    if (active == null) {
	      scheduleNext();
	    }
	  }
	
	  protected synchronized void scheduleNext() {		
	    if ((active = tasks.poll()) != null) {
	      executor.execute(active);
	    }
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	  }
}
