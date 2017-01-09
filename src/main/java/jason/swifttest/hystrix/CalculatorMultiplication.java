package jason.swifttest.hystrix;

import com.google.common.util.concurrent.ListenableFuture;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import jason.swifttest.Calculator;
import rx.AsyncEmitter;
import rx.AsyncEmitter.BackpressureMode;
import rx.Observable;
import rx.functions.Action1;

public class CalculatorMultiplication   extends HystrixObservableCommand<Integer>{

	int left;
	int right;
	public CalculatorMultiplication (int left, int right){
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("calculator"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("multiplication"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(4000))
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
						ListenableFuture<Integer> future=client.multiplication(left, right);
						future.addListener(()->{
							try  {
								t.onNext(future.get());
								t.onCompleted();
								client.close();
							} catch (Exception e1){
								t.onError(e1);
								try  {
									client.close();
								} catch (Exception x){
									
								}
							}
						}, ClientFactory.getInstance().executor);
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

	@Override
	protected Observable<Integer> resumeWithFallback() {
		System.out.println("**********fall back is called for multiplication");
		return Observable.just(0);
	}
	
	
}
