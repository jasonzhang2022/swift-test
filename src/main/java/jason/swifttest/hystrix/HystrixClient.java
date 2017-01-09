package jason.swifttest.hystrix;

import java.util.concurrent.ExecutionException;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class HystrixClient {

	public static void main(String args[]) throws InterruptedException, ExecutionException {
		// cache test
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try {
			
		
			CalculatorAdder adder = new CalculatorAdder(3, 5);
			CalculatorMultiplication times = new CalculatorMultiplication(3, 5);
		
			CalculatorAdder adder1 = new CalculatorAdder(3, 10);
			CalculatorMultiplication times1 = new CalculatorMultiplication(3, 10);
			
			
			
			// regular invocation
			System.out.println("--------------basic test --------------------");
			System.out.printf(" 3+5=%d\n", adder.execute());
			times.toObservable().subscribe((product) -> {
				System.out.printf("3*5=%d: from fallback: %s: failed:%s\n", product,
						times.isResponseFromFallback() ? "true" : "false",
						times.isFailedExecution()?"true":"false"
						
						);
			}, ex -> {
				ex.printStackTrace();
			});

			// fackback test

			System.out.println("--------------fallback test. result should be zero------------------");

			System.out.printf(" 3+10=%d: from fallback: %s: failed:%s\n", adder1.execute(),
							adder1.isResponseFromFallback() ? "true" : "false", 
							adder1.isFailedExecution()?"true":"false");
			
			times1.toObservable().subscribe((product) -> {
				System.out.printf("3*10=%d: from fallback: %s: failed:%s\n", product,
						times1.isResponseFromFallback() ? "true" : "false",
						times1.isFailedExecution()?"true":"false"		
						);
			}, ex -> {
				ex.printStackTrace();
			});
		} finally {
			context.shutdown();
		}

	}

}
