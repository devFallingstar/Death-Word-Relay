package RoomServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import Data.Room;
import Data.User;

/**
 * This class is used for Handling each user with thread.
 * 
 * Also, process almost of message from client-side, 
 * and send almost of message 
 * to client-side.
 * 
 * Also, process almost of message from client-side, 
 * and send almost of message
 * to client-side.
 * 
 * @author YYS
 *
 */
public class UserHandler extends Thread {
	/**
	 * Sockets and streams for server-client communication
	 */
	private Socket socket;
	private Socket dataSocket;
	private BufferedReader in;
	private PrintWriter out;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	
	/**
	 * Basic information for users
	 */
	private String name;
	private User myUser;
	private User myOppUser;

	/**
	 * Timer for each user's game rounds.
	 */
	Timer gameStartTimer;
	TimerTask gameStartTask;
	int timerInt = 6;

	/**
	 * Answer that current user wrote just right now.
	 */
	private String currentAnswer;

	/**
	 * HashSet for users that joining.
	 */
	public static HashSet<String> names = new HashSet<String>();
	public static HashSet<User> users = new HashSet<User>();

	/**
	 * Construct  for UserHandler class.
	 * It will get a sockets for one client.
	 * Most of methods will be done in run() method.
	 * 
	 */
	public UserHandler(Socket _socket, Socket _dataSocket) {
		this.socket = _socket;
		this.dataSocket = _dataSocket;
		new Server();
	}

	/**
	 * Services current user client
	 * by requesting the name and broadcasting informations, 
	 * with input and output streams.
	 * 
	 */
	public void run() {
		try {
			/* Create streams for the socket. */
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			objOut = new ObjectOutputStream(dataSocket.getOutputStream());
			objIn = new ObjectInputStream(dataSocket.getInputStream());

			/*
			 * Keep requesting for user's name that is not already used.
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
			 * If name is accepted, add it to user HashSet to manage its stream
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

			/* Notice about new room list every 3 seconds */
			Timer roomReloadTimer = new Timer();
			TimerTask roomReloadTask = new TimerTask() {
				@Override
				public void run() {
					out.println("NEWROOMAVAIL");
				}
			};
			roomReloadTimer.schedule(roomReloadTask, 1000, 3000);

			/*
			 * wait for the message from client and send it to others. If User
			 * sends message in a room, Check its room number, and send the
			 * message to room members only.
			 */
			while (true) {
				String input = in.readLine();
				
				if (input == null || input.trim().isEmpty()) {
					return;
				}
				input = input.trim();
				input = new String(input.getBytes("utf-8"));
				
				if (input.startsWith("MAKEROOM ")) {
					String roomTitle = input.substring(9);
					int roomNo = Server.addRoom(roomTitle);
					this.myUser.setrNo(roomNo);

					out.println("ROOMMADE " + roomNo);
				} else if (input.startsWith("EXITROOM")) {
					Server.removeUserFromRoom(myUser);

				} else if (input.startsWith("REQROOMINFO ")) {
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

					if (!(input.substring(9).isEmpty())) {
						Server.broadCast(name + " : " + input.substring(9).trim(), roomNo);
					}
				} else if (input.startsWith("ROOMANS ")) {
					StringTokenizer toks = new StringTokenizer(input.substring(8), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);

					currentAnswer = input.substring(9);
					if(!currentAnswer.isEmpty() || !currentAnswer.startsWith("\n")){
						Server.broadCast(name + " : " + currentAnswer.trim(), roomNo);
					}
				} else if (input.startsWith("COMESUCC")) {
					Server.addUserToRoom(this.myUser);
				} else if (input.startsWith("READY")) {
					myOppUser = Server.getOppUser(myUser);
					myUser.setReady();

					if (myOppUser != null) {
						myOppUser.getOut().println("ROOMMSG [SYSTEM] Opposite user is ready!");
					}

					try {
						if (myOppUser.getReady()) {
							out.println("PLAYGAME");
							myOppUser.getOut().println("PLAYGAME");
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

					if (!currentRoomM.isNotDup(currentAnswer.trim())) {
						out.println("DUPWORD");
					} else {
						currentRoomM.addPrevWord(currentAnswer.trim());
						setResume();
					}
				} else if (input.startsWith("ILOSEROUND")) {
					if(input.contains("TIMEOUT")){
						WhenLose(true);
					}else{
						WhenLose(false);
					}
					
					if (FinGame()) {
						out.println("GAMEFIN");
						myOppUser.getOut().println("GAMEFIN");

						myUser.getOut().println("LOSEGAME");
						myOppUser.getOut().println("WINGAME");
						
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
				} else if (input.startsWith("REQOPPUSER")){
					myOppUser = Server.getOppUser(myUser);
					if(myOppUser != null){
						myUser.getOut().println("OPPUSER "+myOppUser.getName());
					}
				} else if (input.startsWith("MESSAGE ")) {
					if (!(input.substring(8).isEmpty())) {
						broadCast(name + ": " + input.substring(8));
						System.out.println(
								"LOG : " + input + " (By. " + name + " With room number " + myUser.getrNo() + ")");
					}
				}
			}
		} catch (SocketException e1){
			System.out.println("ERROR : User connect refused!");
			removeUser();
		} catch (Exception e) {
			e.printStackTrace();
			removeUser();
		} finally {
			removeUser();
		}
	}

	/**
	 * Remove current user from server
	 */
	private void removeUser(){
		if(myUser.getPlaying() == true){
			myUser.getOut().println("LOSEGAME");
			myOppUser.getOut().println("GAMEFIN");
			myOppUser.setUnPlaying();
			myOppUser.setUnReady();
			myOppUser.InitRoundScore();
		}
		
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
		myUser.getOut().println("SETNEWROUND");
		myOppUser.getOut().println("SETNEWROUND");

		Server.setStartOfRoom(myUser.getrNo());

		gameStartTimer = new Timer();
		gameStartTask = new TimerTask() {
			@Override
			public void run() {
				Server.getRoomWithNumber(myUser.getrNo()).startGame();
			}
		};

		gameStartTimer.schedule(gameStartTask, 5000);
	}

	public void setResume() {
		Server.getRoomWithNumber(myUser.getrNo()).resumeGame();
	}

	public void WhenLose(boolean isTimeOut) {
		User loseUser = myUser;
		User winUser = myOppUser;

		loseUser.Lose();
		winUser.Win();
		
		if(isTimeOut){
			loseUser.getOut().println("ILOSEROUND TIMEOUT");
			winUser.getOut().println("IWINROUND TIMEOUT");
		}else{
			loseUser.getOut().println("ILOSEROUND");
			winUser.getOut().println("IWINROUND");
		}
	}

	public boolean FinGame() {
		boolean isFinished = myUser.isFin();

		return isFinished;
	}
}

