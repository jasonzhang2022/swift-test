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
/*
 * Works, Need to manage timeout
 */
public class MultiplexClientAsync {

	// run multiple addtion at the same time by multiple thread
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

		ThriftClientManager clientManager = new ThriftClientManager();
		InetSocketAddress remote = new InetSocketAddress("localhost", 8000);
		FramedClientConnector connector = new FramedClientConnector(remote);

		int len = 8;

		CompletableFuture<Integer>[] multiplications = new CompletableFuture[len];
		for (int i = 0; i < len; i++) {
			multiplications[i] = new CompletableFuture<Integer>();
		}

		for (int i = 0; i < len; i++) {
			final int f = i;
			executor.schedule(() -> {
				ListenableFuture<Calculator> calc = clientManager.createClient(connector, Calculator.class);

				try {

					// Each thread has to use its won client.
					Calculator c = calc.get();
					ListenableFuture<Integer> prod = c.multiplication(f, f);
					prod.addListener(() -> {

						try {
							int result = prod.get();
							System.out.printf("client: %d * %d = %d\n", f, f, result);
							multiplications[f].complete(result);
							c.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}, executor);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}, 1, TimeUnit.SECONDS);
		}

		CompletableFuture.allOf(multiplications).thenRunAsync(() -> {
			clientManager.close();
			for (int i = 0; i < len; i++) {
				try {
					System.out.printf(" %d * %d = %d\n", i, i, multiplications[i].get());
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
