
public class User {
	private String phoneNumber;		//사용자 전화번호
	
	//생성자
	User(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	//getter & setter
	void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	String getPhoneNumber() {
		return phoneNumber;
	}
}
