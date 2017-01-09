package jason.swifttest.hystrix;

import java.util.concurrent.ExecutionException;

public class HystrixClient {
	
	public static void main(String args[]) throws InterruptedException, ExecutionException{
		
		
		System.out.printf(" 3+5=%d\n", new CalculatorAdder(5, 3).construct().toBlocking().toFuture().get());
		new CalculatorMultiplication(3, 5).construct().subscribe((product)->{
			System.out.printf("3*5=%d\n", product);
		}, ex->{
			ex.printStackTrace();
		});
		
		
	}

}
