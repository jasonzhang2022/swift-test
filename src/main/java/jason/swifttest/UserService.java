package jason.swifttest;

import java.util.Date;


public class UserService implements IUserService{
	

	public User getUser(String id){	
		User user = new User();
		user.setFirstName("firstName");
		user.setLastName("last name");
		user.setAge(50);
		user.setBirthDate(new Date());
		System.out.printf("server: send  %s for %s\n", user.toString(), id);
		return user;
	}
	

	public void deleteUser(String id){
		System.out.printf("server: delete user is called for %s\n", id);
		
	}


	@Override
	public void close() throws Exception {
		
	}

}
