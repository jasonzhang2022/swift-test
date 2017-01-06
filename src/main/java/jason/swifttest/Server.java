package jason.swifttest;

import java.util.Collections;

import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServerConfig;
import com.facebook.swift.service.ThriftServiceProcessor;

public class Server {
	
	public static void main(String[] args){
		

		ThriftServiceProcessor processor = new ThriftServiceProcessor(new ThriftCodecManager(), Collections.EMPTY_LIST, new UserService(), new CalculatorImpl());
		ThriftServer server = new ThriftServer(processor, new ThriftServerConfig().setPort(8000));
		server.start();
	}

}
