import java.util.*;
import java.io.*;

public class StudyRoomManagement {
	private Room[] roomArray = new Room[100];		//방 객체 배열.
	private int roomCount = 0;						//생성된 방의 수.
	private String managerID;								//관리자 id

	
	//생성자
	StudyRoomManagement(String managerID, DataInputStream OI) throws Exception {
		this.managerID = managerID;
		
		String roomInfoBuff[] = new String[6];
		String stringBuff = "";
		int i = 0;
		while(true) {
			int data = OI.read();
			if(data == -1) break;
			else if((char)data == ' ') {
				roomInfoBuff[i++] = stringBuff;
				stringBuff = "";
			} else if((char)data == '\n') {
				roomArray[roomCount] = new Room(roomInfoBuff[0], Integer.parseInt(roomInfoBuff[1]), Integer.parseInt(roomInfoBuff[2]));
				if(roomInfoBuff[3].equals("true")) {
					User user = new User(roomInfoBuff[4]);
					String calendarBuff[] = roomInfoBuff[5].split(":");
					GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(calendarBuff[0]), Integer.parseInt(calendarBuff[1]), Integer.parseInt(calendarBuff[2]), Integer.parseInt(calendarBuff[3]), Integer.parseInt(calendarBuff[4]));
					roomArray[roomCount].checkIn(user, calendar);
				}
				i=0;
				roomCount++;
			}
			else {
				stringBuff += (char)data;
			}
		}
	}

	//managerID setter(getter은 보안을 위해 사용하지 않음)
	void setManagerID(String changeID) {
		this.managerID = changeID;
	}
	
	//managerID가 일치하는지 확인
	boolean matchMangerID(String managerID) {
		return (this.managerID.equals(managerID));
	}
	
	//방 크기에 따른 "빈방" 찾기
	Room[] searchEmptyRoom(int size) throws Exception {
		Room[] buff = new Room[100];
		int buff_size = 0;
		boolean foundRoom = false;
		
		for(int i = 0; i< roomCount;i++) {
			Room room = roomArray[i];
			if(room.getCapacity() >= size && !room.isRoomUsed()) {	//입력된 방의 크기 이상의 capacity의 방을 출력
				buff[buff_size++] = room;
				foundRoom = true;
			}
		}
		
		if(!foundRoom)	//찾지 못했을 시에 예외 처리
			throw new Exception("No Available Room");
		
		return buff;
	}
	//모든 방 찾기
	Room[] searchRoom() throws Exception {
		if(roomCount == 0)
			throw new Exception("No Room");
		else 
			return roomArray;  
	}
	
	//방 생성
	void makeRoom(Room newRoom) {
		roomArray[roomCount++] = newRoom;
	}
	//방 삭제
	Room deleteRoom(String roomName) throws Exception{
		
		boolean foundRoom = false;
		for(int i = 0; i< roomCount;i++) {
			Room room = roomArray[i];
			if(roomName.equals(room.getRoomName())) {
				foundRoom = true;
				for(int j = i;j<roomCount;j++) {
					roomArray[i] = roomArray[i+1];
				}
				roomCount--;
				return room;
			}
		}
		
		if(!foundRoom)	//찾지 못했을 시 예외 처리
			throw new Exception("There is no " + roomName + " Room");
		
		return null;
	}
	
	//방 사용(check-in) (수정필요 >> searchRoom 메소드 사용, UI에서 exception 사용)
	void checkIn(String roomName, User user) throws Exception{
		boolean foundRoom = false;
		for(int i = 0; i< roomCount;i++) {
			Room room = roomArray[i];
			if(roomName.equals(room.getRoomName())) {
				room.checkIn(user);
				foundRoom = true;
			}
		}
		
		if(!foundRoom)
			throw new Exception("There is no " + roomName + " Room");
	}
	//방 사용 종료 및 사용 금액 반환
	int checkOut(User user) throws Exception{
		boolean foundRoom = false;
		int payBill = 0;
		for(int i = 0; i<roomCount;i++) {
			Room room = roomArray[i];
			if(room.isRoomUsed() && room.getPhoneNum().equals(user.getPhoneNumber())) {
				payBill = room.checkOut();
				foundRoom = true;
				GregorianCalendar date = new GregorianCalendar();
			}
		}
		
		if(!foundRoom) 
			throw new Exception("There is no room for user " + user.getPhoneNumber());
			
		return payBill;
	}
	
	//방정보 저장, DataOutputStream은 UI에서 생성
	void writeRoomInfos(BufferedWriter output) throws Exception {
		for(int i = 0;i<roomCount;i++) {
			roomArray[i].writeRoomInfo(output);
			output.write("\n");
		}
	}
}
