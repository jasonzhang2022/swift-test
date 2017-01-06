package jason.swifttest;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.util.concurrent.ListenableFuture;

public class AsynchronousClient {
	
	
	public static void main(String[] args) {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		ThriftClientManager clientManager = new ThriftClientManager();
		InetSocketAddress remote = new InetSocketAddress("localhost", 8000);
		FramedClientConnector connector = new FramedClientConnector(remote);
		try {
			Calculator calc = clientManager.createClient(connector, Calculator.class).get();
			
			// asynchronous call
			ListenableFuture<Integer> future = calc.multiplication(5, 3);
			future.addListener(() -> {
				try {
					System.out.printf("5*3 = %d\n", future.get());
					calc.close();
					clientManager.close();
					executor.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, executor);

			// have to have the thread alive to receive asyncchronous
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
