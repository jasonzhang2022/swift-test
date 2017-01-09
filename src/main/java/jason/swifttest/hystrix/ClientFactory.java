package jason.swifttest.hystrix;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.util.concurrent.ListenableFuture;

import rx.AsyncEmitter;
import rx.AsyncEmitter.BackpressureMode;
import rx.Observable;
import rx.functions.Action1;

public class ClientFactory {

	
	public ExecutorService executor = Executors.newCachedThreadPool();
	ThriftClientManager clientManager = new ThriftClientManager();
	InetSocketAddress remote = new InetSocketAddress("localhost", 8000);
	FramedClientConnector connector = new FramedClientConnector(remote);
	
	private ClientFactory(){
		
	}
	
	
	public static ClientFactory getInstance(){
		return instance;
	}
	
	static ClientFactory instance=new ClientFactory();
	
	
	public <T> Observable<T> getClient(Class<T> clasz) {
		
		return Observable.fromEmitter(new Action1<AsyncEmitter<T>>(){

			@Override
			public void call(AsyncEmitter<T> t) {
				ListenableFuture<T> future=clientManager.createClient(connector, clasz);
				
				future.addListener(()->{
					try  {
						t.onNext(future.get());
						t.onCompleted();
					} catch (Exception e){
						t.onError(e);
					}
				}, executor);
				
				
			}
		}, BackpressureMode.NONE);
	}
	
}
