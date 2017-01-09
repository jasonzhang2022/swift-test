package jason.swifttest;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class CalculatorImpl implements Calculator {


	public int plus(int left, int right) {
		System.out.printf("return sum for %d+ %d\n ", left, right);
		
		if (right==10){
			throw new RuntimeException("cann't add");
		}
		return left + right;
	}

	public ListenableFuture<Integer> multiplication(int left, int right) {
		
		ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(1));
		return executorService.submit( ()-> { 
			
			if (right==10){
				Thread.currentThread().sleep(5000);
			} else {
				Thread.currentThread().sleep(3000);
			}
			System.out.printf("return product for %d * %d \n ", left, right);
			return left*right;
			});
	}

	@Override
	public void close() throws Exception {
		
	}


}
