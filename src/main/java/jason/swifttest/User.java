package jason.swifttest;

import java.util.Date;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

@ThriftStruct
public class User {
	
	@ThriftField(1)
	public String firstName;
	
	@ThriftField(2)
	public String lastName;
	
	@ThriftField(3)
	public int age;
	
	Date birthDate;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@ThriftField(4)
	public long getBirthDateThrift(){
		return birthDate==null?0:birthDate.getTime();
	}
	@ThriftField
	public void setBirthDateThrift(long time){
		birthDate= time==0?null:new Date(time);
	}
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", birthDate=" + birthDate
				+ "]";
	}
	
	

}
