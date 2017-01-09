package jason.swifttest.hystrix;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class HystrixClientCache {

	public static void main(String[] args) {
		// cache test
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try {
			CalculatorAdder adder = new CalculatorAdder(3, 5);

			System.out.printf(" 3+5=%d: from cache:%s\n", adder.execute(), adder.isResponseFromCache() ? "true" : "false");
			
			CalculatorAdder adder1 = new CalculatorAdder(3, 5);
			System.out.printf(" 3+5=%d: from cache:%s\n", adder1.execute(), adder1.isResponseFromCache() ? "true" : "false");

		} finally {
			context.shutdown();
			Hystrix.reset();
			System.out.println("shutdown");
		}

	}

}
