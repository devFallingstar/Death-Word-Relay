
package RoomServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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
	private InputStream sin;
	private OutputStream sout;
	private User myUser;
	private Server myServer;

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
		this.myServer = new Server();
	}

	/*
	 * Services this user client by requesting the name and broadcasting, with
	 * input and output streams.
	 * 
	 */
	public void run() {
		System.out.println(this.socket.isConnected() + " | " + this.dataSocket.isConnected());
		try {
			// Create streams for the socket.
			// in = new BufferedReader(new InputStreamReader(
			// socket.getInputStream()));
			// out = new PrintWriter(socket.getOutputStream(), true);
			// objIn = new ObjectInputStream(dataSocket.getInputStream());
			// objOut = new ObjectOutputStream(dataSocket.getOutputStream());
			
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
				System.out.println("Somebody come in");

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
			}
			System.out.println(users);

			myUser = new User(name, in, out);
			Server.addUser(myUser);

			broadCast("MESSAGE [SYSTEM] User [" + name + "] is connected.");
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
			roomReloadTimer.schedule(roomReloadTask, 0, 7000);
			
			while (true) {
				String input = in.readLine();
				if (input == null) {
					return;
				}
				if (input.startsWith("ENTERROOM ")) {

				} else if (input.startsWith("MAKEROOM ")) {
					String roomTitle = input.substring(9);
					int roomNo = Server.addRoom(roomTitle);
					this.myUser.setrNo(roomNo);
					
					out.println("ROOMMADE " + roomNo);
				} else if (input.startsWith("EXITROOM")) {
					Server.removeUserFromRoom(myUser);
				} else if (input.startsWith("REQROOMINFO ")) {
					
					// TODO 방에 2명있으면 정보 안주고, 1명이하면 정보 주는거 구현하기.
					StringTokenizer toks = new StringTokenizer(input.substring(12), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);
					Room curRoom = getRoomInfo(roomNo);
					
					String[] roomInfo = {"", ""};
					roomInfo[0] = curRoom.getNo()+"";
					roomInfo[1] = curRoom.getName();
					
					objOut.reset();
					objOut.writeObject(roomInfo);
					objOut.flush();
					
					Server.addUserToRoom(this.myUser);
//					out.println("COMEINTO " + roomNo);
//					System.out.println("COMEINTO " + roomNo);
					
				} else if (input.equals("REQROOMLIST")) {
					HashMap<Integer, String> newRoomList = Server.getRoomList();
					
					objOut.reset();
					objOut.writeObject(newRoomList);
					objOut.flush();
				} else if (input.startsWith("ROOMMSG ")) {
					StringTokenizer toks = new StringTokenizer(input.substring(8), " ");
					String roomNoStr = toks.nextToken();
					String msg = input.substring(9);
					int roomNo = Integer.parseInt(roomNoStr);
					
					Server.broadCast(msg, roomNo);
				} else if (input.startsWith("MESSAGE ")){
					broadCast("MESSAGE " + name + ": " + input.substring(8));
					System.out.println("LOG : [" + name + "] : " + input);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (name != null) {
				names.remove(name);
				broadCast("MESSAGE User [" + name + "] is disconnected.");
				System.out.println("LOG : User [" + name + "] disconnected.");
			}
			if (out != null) {
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private Room getRoomInfo(int rNo) {
		RoomManager newRoomM = Server.getRoomWithNumber(rNo);
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
		// for(User bUser : users){
		// bUser.getOut().println(msg);
		// }
	}

	public void giveRoomInfo() {
		// TODO Server.java의 roomMap에서 번호에 해당하는 RoomManager를 가져와야함.
		// RoomManager myRoom = myServer.getRoomWithNumber();

	}

}
