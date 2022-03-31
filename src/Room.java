import java.util.*;
import java.io.*;

public class Room {
	private int capacity; // 최대 인원 수
	private String roomName; // 방 이름
	private int bill; // 시간 당 가격

	// check-in, check-out 시 사용
	private boolean isUsed; // 사용중인지
	private User user; // 사용중인 사용자 객체
	private Calendar startTime; // 사용 시작 시간(사용 종료 시간은 생략)

	// 생성자
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

	// 체크인
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

	// 체크아웃
	int checkOut() {
		int payBill = pay(startTime, new GregorianCalendar());
		isUsed = false;
		user = null;
		return payBill;
	}

	// 지불해야할 금액 설정(수정 필요 >> Management로 이동)
	int pay(Calendar start, Calendar end) {
		int usedTime = 0; // 총 사용시간(시간으로 계산. ex 2시간 30분 사용 >> usedTime = 3)

		// 날짜를 넘어갔을 경우
		if (start.get(Calendar.DATE) != end.get(Calendar.DATE)) {
			// 날이 지나가고, 한시간 이내로 사용했을 경우
			if (start.get(Calendar.HOUR_OF_DAY) == 23 && end.get(Calendar.HOUR_OF_DAY) == 0
					&& start.get(Calendar.MINUTE) + end.get(Calendar.MINUTE) <= 60) {
				usedTime += 1;
			}
			// 나머지 경우
			else {
				// 기본 시간
				usedTime = 24 - start.get(Calendar.HOUR_OF_DAY) + end.get(Calendar.HOUR_OF_DAY);
				// 사용한 분 고려
				if ((60 - start.get(Calendar.MINUTE)) + end.get(Calendar.MINUTE) > 60)
					usedTime += 1;
			}

		}
		// 그 이외의 경우
		else {
			// 기본 시간
			usedTime = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
			// 사용한 분 고려
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
