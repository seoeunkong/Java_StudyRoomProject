import java.util.*;
import java.io.*;

public class StudyRoomManagement {
	private Room[] roomArray = new Room[100];		//�� ��ü �迭.
	private int roomCount = 0;						//������ ���� ��.
	private String managerID;								//������ id

	
	//������
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

	//managerID setter(getter�� ������ ���� ������� ����)
	void setManagerID(String changeID) {
		this.managerID = changeID;
	}
	
	//managerID�� ��ġ�ϴ��� Ȯ��
	boolean matchMangerID(String managerID) {
		return (this.managerID.equals(managerID));
	}
	
	//�� ũ�⿡ ���� "���" ã��
	Room[] searchEmptyRoom(int size) throws Exception {
		Room[] buff = new Room[100];
		int buff_size = 0;
		boolean foundRoom = false;
		
		for(int i = 0; i< roomCount;i++) {
			Room room = roomArray[i];
			if(room.getCapacity() >= size && !room.isRoomUsed()) {	//�Էµ� ���� ũ�� �̻��� capacity�� ���� ���
				buff[buff_size++] = room;
				foundRoom = true;
			}
		}
		
		if(!foundRoom)	//ã�� ������ �ÿ� ���� ó��
			throw new Exception("No Available Room");
		
		return buff;
	}
	//��� �� ã��
	Room[] searchRoom() throws Exception {
		if(roomCount == 0)
			throw new Exception("No Room");
		else 
			return roomArray;  
	}
	
	//�� ����
	void makeRoom(Room newRoom) {
		roomArray[roomCount++] = newRoom;
	}
	//�� ����
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
		
		if(!foundRoom)	//ã�� ������ �� ���� ó��
			throw new Exception("There is no " + roomName + " Room");
		
		return null;
	}
	
	//�� ���(check-in) (�����ʿ� >> searchRoom �޼ҵ� ���, UI���� exception ���)
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
	//�� ��� ���� �� ��� �ݾ� ��ȯ
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
	
	//������ ����, DataOutputStream�� UI���� ����
	void writeRoomInfos(BufferedWriter output) throws Exception {
		for(int i = 0;i<roomCount;i++) {
			roomArray[i].writeRoomInfo(output);
			output.write("\n");
		}
	}
}
