import java.util.*;
import java.io.*;

public class Room {
	private int capacity; // �ִ� �ο� ��
	private String roomName; // �� �̸�
	private int bill; // �ð� �� ����

	// check-in, check-out �� ���
	private boolean isUsed; // ���������
	private User user; // ������� ����� ��ü
	private Calendar startTime; // ��� ���� �ð�(��� ���� �ð��� ����)

	// ������
	Room(String roomName, int capacity, int bill) {
		this.capacity = capacity;
		this.roomName = roomName;
		this.bill = bill;
		this.isUsed = false;
	}

	// getter & setter
	int getCapacity() {
		return capacity;
	}

	String getRoomName() {
		return roomName;
	}

	int getBill() {
		return bill;
	}

	String getPhoneNum() {
		return user.getPhoneNumber();
	}

	void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	void setBill(int bill) {
		this.bill = bill;
	}

	boolean isRoomUsed() {
		return isUsed;
	}

	// üũ��
	void checkIn(User user) {
		this.user = user;
		startTime = new GregorianCalendar();
		isUsed = true;
	}

	void checkIn(User user, Calendar startTime) {
		this.user = user;
		this.startTime = startTime;
		isUsed = true;
	}

	// üũ�ƿ�
	int checkOut() {
		int payBill = pay(startTime, new GregorianCalendar());
		isUsed = false;
		user = null;
		return payBill;
	}

	// �����ؾ��� �ݾ� ����(���� �ʿ� >> Management�� �̵�)
	int pay(Calendar start, Calendar end) {
		int usedTime = 0; // �� ���ð�(�ð����� ���. ex 2�ð� 30�� ��� >> usedTime = 3)

		// ��¥�� �Ѿ�� ���
		if (start.get(Calendar.DATE) != end.get(Calendar.DATE)) {
			// ���� ��������, �ѽð� �̳��� ������� ���
			if (start.get(Calendar.HOUR_OF_DAY) == 23 && end.get(Calendar.HOUR_OF_DAY) == 0
					&& start.get(Calendar.MINUTE) + end.get(Calendar.MINUTE) <= 60) {
				usedTime += 1;
			}
			// ������ ���
			else {
				// �⺻ �ð�
				usedTime = 24 - start.get(Calendar.HOUR_OF_DAY) + end.get(Calendar.HOUR_OF_DAY);
				// ����� �� ���
				if ((60 - start.get(Calendar.MINUTE)) + end.get(Calendar.MINUTE) > 60)
					usedTime += 1;
			}

		}
		// �� �̿��� ���
		else {
			// �⺻ �ð�
			usedTime = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
			// ����� �� ���
			if ((60 - start.get(Calendar.MINUTE)) + end.get(Calendar.MINUTE) > 60
					|| start.get(Calendar.MINUTE) - end.get(Calendar.MINUTE) == 0)
				usedTime += 1;
		}
		return usedTime * bill;
	}

	void writeRoomInfo(BufferedWriter output) throws Exception {
		output.write(roomName + " ");
		output.write(Integer.toString(capacity));
		output.write(" ");
		output.write(Integer.toString(bill));
		output.write(" ");

		output.write(isUsed ? "true" : "false");
		output.write(" ");
		output.write(isUsed ? this.getPhoneNum() : "NaN");
		output.write(" ");
		output.write(isUsed ? Integer.toString(startTime.get(Calendar.YEAR)) + ":"
				+ Integer.toString(startTime.get(Calendar.MONTH)) + ":" + Integer.toString(startTime.get(Calendar.DATE))
				+ ":" + Integer.toString(startTime.get(Calendar.HOUR_OF_DAY)) + ":"
				+ Integer.toString(startTime.get(Calendar.MINUTE)) : "NaN");
		output.write(" ");
	}
}
