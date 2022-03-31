import java.io.*;
import java.util.*;

public class StudyRoomUI {

	public static void main(String[] args) {
		// Scanner 설정
		Scanner scan = new Scanner(System.in);

		StudyRoomManagement SRM;

		try {
			//파일 생성
			File file = new File("C://StudyRoom//RoomInfo.txt");
			file.createNewFile();
			DataInputStream inputFile = null;
			inputFile = new DataInputStream(new FileInputStream(file));
			SRM = new StudyRoomManagement("manager123", inputFile);

			// mode 변경을 위한 변수
			boolean goodBye = false;

			while (!goodBye) {
				// mode 변경을 위한 변수
				boolean isUser = false, isManager = false;

				// 사용자, 관리자 선택
				System.out.println("####WELCOME TO STUDYROOM####");
				System.out.println("1. user mode");
				System.out.println("2. manager mode");
				System.out.println("3. exit");

				// mode를 받았는지 확인
				boolean gotMode = false;
				while (!gotMode) {
					try {
						System.out.print("#: ");
						int num = scan.nextInt();

						switch (num) {
						case 1:
							isUser = true;
							gotMode = true;
							break;
						case 2:
							isManager = true;
							gotMode = true;
							break;
						case 3:
							goodBye = true;
							gotMode = true;
							break;
						default:
							System.out.println("Wrong mode. Please choose again");
							break;
						}
					} catch (InputMismatchException e) {
						scan = new Scanner(System.in);
						System.out.println("Only Numbers are available");
					}
				}

				// ###각 mode마다 처리

				boolean exit = false;
				// User 모드
				if (isUser) {
					// User 정보 입력
					System.out.println("##To check-in, Please enter your PhoneNumber");
					System.out.print("#: ");
					scan.nextLine(); // 버퍼 지우기
					String phoneNum = scan.nextLine();
					User user = new User(phoneNum);

					// User Menu
					while (!exit) {

						try {
							// 추후 추가 예정
							System.out.println("####Menu####");
							System.out.println("1. Search Room");
							System.out.println("2. Use Room");
							System.out.println("3. Check Out");
							System.out.println("4. exit to main");
							System.out.print("#: ");
							int num = scan.nextInt();

							switch (num) {
							case 1: // 빈 방 찾기
								System.out.println("##Search Room##");
								System.out.print("Enter size: ");

								try {
									int roomSize = scan.nextInt();
									Room[] Room = SRM.searchEmptyRoom(roomSize);
									System.out.println("Available Rooms are");
									System.out.println("num\tName\tCapacity\tBill");
									for (int i = 0; i < Room.length; i++) {
										Room room = Room[i];
										if (room == null)
											break;
										else {
											System.out.println((i + 1) + "|\t" + room.getRoomName() + "\t"
													+ room.getCapacity() + "\t\t" + room.getBill());
										}
									}
								} catch (InputMismatchException e) {
									scan = new Scanner(System.in);
									System.out.print("Only Numbers are available");
								} catch (Exception e) {
									// 사용 가능한 방이 없을 경우
									System.out.println(e.getMessage());
								}
								break;

							case 2: // 방 사용하기
								System.out.println("##Use Room##");
								try {
									System.out.print("Enter room name: ");
									scan = new Scanner(System.in);
									String roomName = scan.nextLine();

									SRM.checkIn(roomName, user);
									System.out.println("You are now using room " + roomName);
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
								break;

							case 3: // 체크 아웃
								System.out.println("##Check out##");
								try {
									int payBill = SRM.checkOut(user);
									System.out.println(
											"Your total bill would be " + payBill + "\nThank you. See you again!");
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
								break;

							case 4:
								exit = true;
								break;

							default:
								System.out.println("Wrong Menu. Please choose again");
								break;
							}
							System.out.println();

						} catch (InputMismatchException e) {
							scan = new Scanner(System.in);
							System.out.println("Only Numbers are available");
						}

					}

				}

				// Manager 모드
				else if (isManager && !exit) {
					// ManagerID 입력
					System.out.println("##To manage, Please enter Given managerID");
					System.out.print("#: ");
					scan.nextLine(); // 버퍼 지우기
					String managerID = scan.nextLine();

					// 올바른 managerID를 입력했는지 판별
					boolean correctID = false;
					while (!correctID && !exit) {
						if (SRM.matchMangerID(managerID)) {
							correctID = true;
							System.out.println("Welcome back, Manager");
						} else {
							System.out.println("Wrong ID! Please try again or enter -1 to exit to main.");
							System.out.print("#: ");
							managerID = scan.nextLine();
							if (managerID.equals(new String("-1")))
								exit = true;
						}
					}

					// User Menu
					while (!exit) {

						try {
							// 추후 추가 예정
							System.out.println("####Menu####");
							System.out.println("1. Make Room");
							System.out.println("2. Delete Room");
							System.out.println("3. View all Room");
							System.out.println("4. exit to main");
							System.out.print("#: ");
							scan = new Scanner(System.in);
							int num = scan.nextInt();

							try {
								switch (num) {
								case 1: // 방 생성
									System.out.println("##Make Room##");
									boolean checkInfo = false;
									String roomName = "";
									int capacity = 0, bill = 0;
									while (!checkInfo) {
										// 방 정보 입력
										System.out.println("#Enter Room Info");
										System.out.print("Room Name: ");
										scan = new Scanner(System.in);
										roomName = scan.nextLine();
										System.out.print("Room Capacity: ");
										capacity = scan.nextInt();
										System.out.print("Bill(per hour): ");
										bill = scan.nextInt();

										// 입력된 정보 확인시키기
										System.out.println("#Enter y/Y if following info is correct");
										System.out.println("Room Name: " + roomName);
										System.out.println("Room Capacity: " + capacity);
										System.out.println("Bill(per hour): " + bill);
										System.out.print("#: ");
										scan = new Scanner(System.in);
										String check = scan.nextLine();
										check = check.toUpperCase();

										checkInfo = check.equals("Y");
									}
									Room tmpRoom = new Room(roomName, capacity, bill);
									SRM.makeRoom(tmpRoom);
									System.out.println("Successfully made room " + roomName);
									break;

								case 2: // 방 삭제
									System.out.println("##Delete Room##");
									System.out.print("Enter room name: ");
									scan = new Scanner(System.in);
									String roomName1 = scan.nextLine();
									SRM.deleteRoom(roomName1);
									System.out.println("Successfully deleted room " + roomName1);
									break;

								case 3: // 모든 방 이름 리스트 뽑기
									System.out.println("##View all Room");
									Room[] Room = SRM.searchRoom();
									System.out.println("Rooms: ");
									System.out.println("num\tName\tCapacity\tBill\tUsing");
									for (int i = 0; i < Room.length; i++) {
										Room room = Room[i];
										if (room == null)
											break;
										else {
											System.out.println(
													(i + 1) + "|\t" + room.getRoomName() + "\t" + room.getCapacity()
															+ "\t\t" + room.getBill() + "\t" + room.isRoomUsed());
										}
									}
									break;

								case 4:
									exit = true;
									break;

								default:
									System.out.println("Wrong Menu. Please choose again");
									break;
								}
							} catch (InputMismatchException e) {
								System.out.println("Please Enter available value");
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
							System.out.println();

						} catch (InputMismatchException e) {
							scan = new Scanner(System.in);
							System.out.println("Only Numbers are available");
						}

					}
				}
			}

			// UI 종료
			System.out.println("Good Bye :)");
			inputFile.close();
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			SRM.writeRoomInfos(output);
			output.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("파일 오류(FileNotFoundException)");
		} catch (IOException e) {
			System.out.println("파일 읽기 오류(IOException)");
		} catch (Exception e) {
			System.out.println("알수 없는 오류"+e.getMessage());
			e.getMessage();
		}

		scan.close();

	}

}
