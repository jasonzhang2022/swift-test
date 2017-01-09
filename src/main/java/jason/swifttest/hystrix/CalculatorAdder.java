package jason.swifttest.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;

import jason.swifttest.Calculator;
import rx.AsyncEmitter;
import rx.AsyncEmitter.BackpressureMode;
import rx.Observable;
import rx.functions.Action1;

public class CalculatorAdder extends HystrixObservableCommand<Integer> {

	
	int left;
	int right;
	public CalculatorAdder(int left, int right){
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("calculator"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("adder"))
				);
		this.left = left;
		this.right = right;
	}
	
	@Override
	protected Observable<Integer> construct() {
		
		
		
		return Observable.fromEmitter(new Action1<AsyncEmitter<Integer>>(){

			@Override
			public void call(AsyncEmitter<Integer> t) {
				ClientFactory.getInstance().getClient(Calculator.class).subscribe(client->{
					
					try  {
						int sum=client.plus(left, right);
						t.onNext(sum);
						t.onCompleted();
						client.close();
					} catch (Exception e){
						t.onError(e);
						try {
							client.close();
						} catch (Exception e1) {
							
						}
					}
					
					
				}, exception->{
					t.onError(exception);
				});
			}
			
		}, BackpressureMode.NONE);
		
	}

}
