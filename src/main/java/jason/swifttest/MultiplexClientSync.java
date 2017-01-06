package jason.swifttest;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.util.concurrent.ListenableFuture;

public class MultiplexClientSync {
	
	//run multiple addtion at the same time by multiple thread
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		
		
		ThriftClientManager clientManager = new ThriftClientManager();
		InetSocketAddress remote = new InetSocketAddress("localhost", 8000);
		FramedClientConnector connector = new FramedClientConnector(remote);
		
		
		CompletableFuture<Integer>[] additions = new CompletableFuture[8];
		for (int i=0; i<8; i++){
			additions[i]=new CompletableFuture<Integer>();
		}
		
		
		for (int i=0; i<8; i++){
			final int f= i;
			executor.schedule(()->{
				ListenableFuture<Calculator> calc = clientManager.createClient(connector, Calculator.class);
				Calculator c;
				try {
					
					//Each thread has to use its won client.
					c = calc.get();
					int result = c.plus(f, f);
					System.out.printf("client: %d + %d = %d\n", f,f, result);
					additions[f].complete(result);
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}, 1, TimeUnit.SECONDS);
		}
		
		CompletableFuture.allOf(additions).thenRunAsync(()->{
			clientManager.close();
			for (int i=0; i<8; i++){
				try {
					System.out.printf(" %d + %d = %d\n",  i, i, additions[i].get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			executor.shutdown();
		}, executor);
		

		executor.awaitTermination(1, TimeUnit.DAYS);
	}

}
