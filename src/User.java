
public class User {
	private String phoneNumber;		//����� ��ȭ��ȣ
	
	//������
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