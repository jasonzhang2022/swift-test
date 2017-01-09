package jason.swifttest.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import jason.swifttest.Calculator;

public class CalculatorAdder extends HystrixCommand<Integer> {

	
	int left;
	int right;
	public CalculatorAdder(int left, int right){
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("calculator"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("adder"))
				);
		this.left = left;
		this.right = right;
	}
	
	
	protected Integer run() throws Exception {
		try  (Calculator calc=ClientFactory.getInstance().getClient(Calculator.class).toBlocking().toFuture().get()){
			return calc.plus(left, right);
		}
	}


	@Override
	protected Integer getFallback() {
		return 0;
	}


	@Override
	protected String getCacheKey() {
	
		return Math.min(left, right)+"+"+Math.max(left, right);
	}
	
	
}
