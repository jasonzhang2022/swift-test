package jason.swifttest;

import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

@ThriftService("userService")
public interface IUserService extends AutoCloseable {
	@ThriftMethod
	public User getUser(String id);
	
	@ThriftMethod
	public void deleteUser(String id);
	

}
