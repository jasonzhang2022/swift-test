package jason.swifttest;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;
import com.google.common.util.concurrent.ListenableFuture;

@ThriftService
public interface Calculator extends AutoCloseable{

	@ThriftMethod
	public int plus(@ThriftField(1) int left, @ThriftField(2) int right);
	
	@ThriftMethod
	public ListenableFuture<Integer> multiplication(int left, int right);
}
