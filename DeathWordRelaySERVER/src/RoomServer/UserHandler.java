
package RoomServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import Data.Room;
import Data.User;

public class UserHandler extends Thread {
	private String name;
	private Socket socket;
	private Socket dataSocket;
	private BufferedReader in;
	private PrintWriter out;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private User myUser;
	private User myOppUser;

	Timer gameCounterTimer;
	TimerTask gameCounterTask;
	Timer gameStartTimer;
	TimerTask gameStartTask;
	int timerInt = 6;

	private String currentAnswer;

	/*
	 * HashSet for users that joining.
	 */
	public static HashSet<String> names = new HashSet<String>();
	public static HashSet<User> users = new HashSet<User>();

	/*
	 * construct handler class, and get a scoket for one client. Also, almost
	 * methods are done in run() method.
	 * 
	 */
	public UserHandler(Socket _socket, Socket _dataSocket) {
		this.socket = _socket;
		this.dataSocket = _dataSocket;
		new Server();
	}

	/*
	 * Services this user client by requesting the name and broadcasting, with
	 * input and output streams.
	 * 
	 */
	public void run() {
		try {
			// Create streams for the socket.
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			objOut = new ObjectOutputStream(dataSocket.getOutputStream());
			objIn = new ObjectInputStream(dataSocket.getInputStream());

			/*
			 * Keep requesting for user's name that is not already used.
			 * 
			 */
			while (true) {
				out.println("SUBMITNAME");
				name = in.readLine();
				if (name == null) {
					this.interrupt();
				}
				synchronized (names) {
					if (!names.contains(name)) {
						names.add(name);
						break;
					} else {
						// TODO response have to add.
						out.println("DUPID ");
						continue;
					}
				}
			}

			/*
			 * If name is accepted, add it to user hashset to manage its stream
			 * for broadcasting a message, and care its room.
			 */
			out.println("NAMEACCEPTED");
			out.println("MESSAGE Welcome to Death Word Relay!");

			if (!isNameExist(name)) {
				User newUser = new User(name, in, out);
				users.add(newUser);
				myUser = newUser;
			} else {
				myUser = new User(name, in, out);
			}
			System.out.println("LOG : User " + myUser.getName() + " connected.");
			System.out.println("LOG : The number of current users : " + users.size());

			Server.addUser(myUser);

			broadCast("[SYSTEM] User [" + name + "] is connected.");
			System.out.println("LOG : User [" + name + "] connected.");

			/*
			 * wait for the message from client and send it to others. If User
			 * sends message in a room, Check its room number, and send the
			 * message to room members only.
			 */
			Timer roomReloadTimer = new Timer();
			TimerTask roomReloadTask = new TimerTask() {
				@Override
				public void run() {
					out.println("NEWROOMAVAIL");
				}
			};
			roomReloadTimer.schedule(roomReloadTask, 1000, 7000);

			while (true) {
				String input = in.readLine();
				if (input == null) {
					return;
				}
				if (input.startsWith("MAKEROOM ")) {
					String roomTitle = input.substring(9);
					int roomNo = Server.addRoom(roomTitle);
					this.myUser.setrNo(roomNo);

					out.println("ROOMMADE " + roomNo);

				} else if (input.startsWith("EXITROOM")) {
					Server.removeUserFromRoom(myUser);

				} else if (input.startsWith("REQROOMINFO ")) {
					// TODO Give info if there's 1 or 0 users. Over 2 user? no
					// info.
					StringTokenizer toks = new StringTokenizer(input.substring(12), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);
					Room curRoom = getRoomInfo(roomNo);

					String[] roomInfo = { "", "" };
					if (curRoom == null) {
						roomInfo[0] = "-1";
						roomInfo[1] = "WTF no room!";
					} else if (curRoom.getRoomMemberNum() >= 2) {
						roomInfo[0] = "-2";
						roomInfo[1] = "Full!";
					} else {
						roomInfo[0] = curRoom.getNo() + "";
						roomInfo[1] = curRoom.getName();
					}

					objOut.reset();
					objOut.writeObject(roomInfo);
					objOut.flush();

					this.myUser.setrNo(roomNo);
				} else if (input.equals("REQROOMLIST")) {
					HashMap<Integer, String> newRoomList = Server.getRoomList();

					objOut.reset();
					objOut.writeObject(newRoomList);
					objOut.flush();

				} else if (input.startsWith("ROOMMSG ")) {
					StringTokenizer toks = new StringTokenizer(input.substring(8), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);

					if (!input.substring(9).startsWith("\n")) {
						Server.broadCast(name + ": " + input.substring(9), roomNo);
					}
				} else if (input.startsWith("COMESUCC")) {
					Server.addUserToRoom(this.myUser);
				} else if (input.startsWith("READY")) {
					User oppUser = Server.getOppUser(myUser);
					myOppUser = oppUser;
					myUser.setReady();

					if (myOppUser != null) {
						myOppUser.getOut().println("ROOMMSG Opposite user is ready!");
					}

					try {
						if (oppUser.getReady()) {
							out.println("PLAYGAME");
							oppUser.getOut().println("PLAYGAME");
							myUser.setPlaying();
							myOppUser.setPlaying();
							setStart();
						}

					} catch (NullPointerException e) {
					}
				} else if (input.startsWith("UNREADY")) {
					myUser.setUnReady();
				} else if (input.startsWith("REQRESUME")) {
					RoomManager currentRoomM = Server.getRoomWithNumber(myUser.getrNo());

					if (!currentRoomM.isNotDup(currentAnswer)) {
						out.println("DUPWORD");
					} else {
						currentRoomM.addPrevWord(currentAnswer);
						setResume();
					}
				} else if (input.startsWith("ROOMANS ")) {
					StringTokenizer toks = new StringTokenizer(input.substring(8), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);

					currentAnswer = input.substring(9);
					Server.broadCast(name + ": " + input.substring(9), roomNo);

				} else if (input.startsWith("ILOSEROUND")) {
					WhenLose();
					if (FinGame()) {
						out.println("GAMEFIN");
						myOppUser.getOut().println("GAMEFIN");

						myUser.getOut().println("LOSEGAME");

						myUser.InitRoundScore();
						myOppUser.InitRoundScore();
						myUser.setUnPlaying();
						myUser.setUnReady();
						myOppUser.setUnPlaying();
						myOppUser.setUnReady();
					} else {
						setStart();
					}
				} else if (input.startsWith("TIMEEND")) {
					out.println("MYTIMEEND");
				} else if (input.startsWith("MESSAGE ")) {
					if (!(input.substring(8).isEmpty() || input.substring(8).startsWith("\n"))) {
						broadCast(name + ": " + input.substring(8));
						System.out.println(
								"LOG : " + input + " (By. " + name + " With room number " + myUser.getrNo() + ")");

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (name != null) {
				broadCast("User [" + name + "] is disconnected.");
				System.out.println("LOG : User [" + name + "] disconnected.");
				names.remove(name);
				if (myUser.getrNo() == -1) {
					Server.removeUser(myUser);
				} else {
					Server.removeUserFromRoom(myUser);

					// TODO If they exit while playing a game, put it to the
					// black list.
				}
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private Room getRoomInfo(int rNo) {
		RoomManager newRoomM = Server.getRoomWithNumber(rNo);
		if (newRoomM == null) {
			return null;
		}
		Room currentRoom = newRoomM.getMyRoom();

		return currentRoom;
	}

	private boolean isNameExist(String name) {
		for (User requestedUser : users) {
			if (requestedUser.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public void broadCast(String msg) {
		Server.broadCast(msg, myUser.getrNo());
	}

	public void setStart() {
		myUser.getOut().println("");
		Server.setStartOfRoom(myUser.getrNo());

		gameStartTimer = new Timer();
		gameStartTask = new TimerTask() {
			@Override
			public void run() {
				Server.getRoomWithNumber(myUser.getrNo()).startGame();
			}
		};

		gameCounterTimer = new Timer();
		gameCounterTask = new TimerTask() {
			@Override
			public void run() {
				timerInt -= 1;
				if (timerInt == 1) {
					Server.broadCast("Start in " + timerInt + " seconds...", myUser.getrNo());
					timerInt = 6;
					gameCounterTimer.cancel();
				} else {
					Server.broadCast("Start in " + timerInt + " seconds...", myUser.getrNo());
				}

			}
		};
		gameCounterTimer.schedule(gameCounterTask, 0, 1000);
		gameStartTimer.schedule(gameStartTask, 5000);
	}

	public void setResume() {
		Server.getRoomWithNumber(myUser.getrNo()).resumeGame();
	}

	public void WhenLose() {
		User loseUser = myUser;
		User winUser = myOppUser;

		loseUser.Lose();
		winUser.Win();

		loseUser.getOut().println("ILOSEROUND");
		winUser.getOut().println("IWINROUND");

		loseUser.getOut().println("SETNEWROUND");
		winUser.getOut().println("SETNEWROUND");
	}

	public boolean FinGame() {
		boolean isFinished = myUser.isFin();

		return isFinished;
	}

	public void sendResultToDB() {

	}
}
