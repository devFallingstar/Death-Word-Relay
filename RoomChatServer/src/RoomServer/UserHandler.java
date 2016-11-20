
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
				System.out.println("LOG : Somebody come in...");

				name = in.readLine();
				if (name == null) {
					continue;
				}
				synchronized (names) {
					if (!names.contains(name)) {
						names.add(name);
						break;
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
			}else{
				myUser = new User(name, in, out);
			}
			System.out.println("LOG : The number of current users : "+users.size());
			
			Server.addUser(myUser);

			broadCast("[SYSTEM] User [" + name + "] is connected.");
			System.out.println("LOG : User [" + name + "] connected.");

			/*
			 * wait for the message from client and send it to others. If User
			 * sends message in a room, Check its room number, and send the
			 * message to room members only.
			 */
			Timer roomReloadTimer = new Timer();
			TimerTask roomReloadTask = new TimerTask(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
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
					// TODO �濡 2�������� ���� ���ְ�, 1�����ϸ� ���� �ִ°� �����ϱ�.
					StringTokenizer toks = new StringTokenizer(input.substring(12), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);
					Room curRoom = getRoomInfo(roomNo);
					
					String[] roomInfo = {"", ""};
					if (curRoom == null){
						roomInfo[0] = "-1";
						roomInfo[1] = "WTF no room!";
					}else{
						roomInfo[0] = curRoom.getNo()+"";
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
					
					Server.broadCast(name + ": " + input.substring(9), roomNo);
				} else if (input.startsWith("COMESUCC")){
					Server.addUserToRoom(this.myUser);
				} else if (input.startsWith("MESSAGE ")){
					broadCast(name + ": " + input.substring(8));
					System.out.println("LOG : "+ input +" (By. "+name+" With room number "+myUser.getrNo()+")");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (name != null) {
				broadCast("User [" + name + "] is disconnected.");
				System.out.println("LOG : User [" + name + "] disconnected.");
				names.remove(name);
				Server.removeUserFromRoom(myUser);
				Server.removeUser(myUser);
//				broadCast("MESSAGE User [" + name + "] is disconnected.");
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private Room getRoomInfo(int rNo) {
		RoomManager newRoomM = Server.getRoomWithNumber(rNo);
		if (newRoomM == null){
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
}