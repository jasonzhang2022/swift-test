package jason.swifttest;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.util.concurrent.ListenableFuture;

public class SynchronousClient {

	
	public static void main(String[] args) {
		
		
		ThriftClientManager clientManager = new ThriftClientManager();
		InetSocketAddress remote = new InetSocketAddress("localhost", 8000);
		FramedClientConnector connector = new FramedClientConnector(remote);
		try  {
			ListenableFuture<IUserService> client1=clientManager.createClient(connector, IUserService.class);
			
			IUserService userService = client1.get();
			User user = userService.getUser("test");
			System.out.println( "client retreived user: "+user);
			
			
			userService.deleteUser("test");
			System.out.println("client : user is deleted");
			userService.close();
			
			Calculator calc = clientManager.createClient(connector, Calculator.class).get();
			System.out.printf(" 5 + 3 = %d\n", calc.plus(5, 3));
			
			calc.close();
		
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			clientManager.close();
		}
	}
}
