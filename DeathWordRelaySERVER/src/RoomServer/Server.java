package RoomServer;

import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

import Data.User;

/**
 * This class is used for main class of server-side system. 
 * It will make a connection between server and client, 
 * and will make streams too.
 * 
 * @author YYS
 *
 */
public class Server {
	/**
	 * ports and sockets for server-client communication
	 */
	private static final int PORT = 9001;
	private static final int DATAPORT = 9002;
	private static ServerSocket dataListener;
	private static ServerSocket listener;

	/**
	 * List of users and rooms
	 */
	private static Vector<User> users = new Vector<User>();
	private static HashMap<Integer, RoomManager> roomMap = new HashMap<Integer, RoomManager>();

	/**
	 * The appplication main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("file.encoding","UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		System.out.println("The chat server is running. with port " + PORT);
		listener = new ServerSocket(PORT);
		dataListener = new ServerSocket(DATAPORT);
		try {
			while (true) {
				UserHandler newUser = new UserHandler(listener.accept(), dataListener.accept());
				newUser.start();
			}
		} finally {
			listener.close();
			dataListener.close();
		}
	}

	/**
	 * Add user to the current server
	 * @param u
	 */
	public static void addUser(User u) {
		users.add(u);
	}
	
	
	/**
	 * Remove user from the current server
	 * @param u
	 */
	public static void removeUser(User u) {
		users.remove(u);
	}

	/**
	 * Add room to the current server with room name. 
	 * @param roomName
	 * @return given room number(ID)
	 */
	public static int addRoom(String roomName) {
		int i = 1;

		while (true) {
			if (roomMap.containsKey(i)) {
				i++;
			} else {
				RoomManager newRoom = new RoomManager(i, roomName);
				roomMap.put(i, newRoom);
				break;
			}
		}
		return i;
	}

	/**
	 * Remove room from the current server with room number(ID)
	 * @param roomID
	 */
	public static void removeRoom(int roomID) {
		roomMap.remove(roomID);
	}

	/**
	 * Add new user to the room with the user's room number.
	 * @param u
	 */
	public static void addUserToRoom(User u) {
		RoomManager destRoom = roomMap.get(u.getrNo());
		destRoom.addUserToRoom(u);

		broadCast("[SYSTEM] " + u.getName() + " connected.", destRoom.getRoomNo());
		destRoom.broadCastRoom("NEWOPP");
	}

	/**
	 * Remove a user from the room.
	 * @param u
	 */
	public static void removeUserFromRoom(User u) {
		RoomManager destRoom = roomMap.get(u.getrNo());
		try {
			destRoom.removeUserFromRoom(u);
			u.setrNo(-1);

			if (destRoom.playerOfRoom() == 0) {
				roomMap.remove(destRoom.getRoomNo());
			} else {
				broadCast("[SYSTEM] " + u.getName() + " disconnected.", destRoom.getRoomNo());
			}
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Broadcast the message to waiting room or game room.
	 * If rNo is -1, it means message will be broadcasted to waiting room,
	 * if not, message will be broadcasted to specific room.
	 * @param msg
	 * @param rNo
	 */
	public static void broadCast(String msg, int rNo) {
		if (rNo == -1) {
			for (User u : users) {
				u.sendMsg("MESSAGE " + msg);
			}
		} else {
			RoomManager newRoom = roomMap.get(rNo);

			newRoom.broadCastRoom("ROOMMSG " + msg);
		}
	}

	/**
	 * Get RoomManager of room that has room number rNo.
	 * @param rNo
	 * @return Appropriate RoomManager instance
	 */
	public static RoomManager getRoomWithNumber(int rNo) {
		RoomManager newRoomM = roomMap.get(rNo);

		return newRoomM;
	}

	/**
	 * Get all room list that running in current server.
	 * @return Room list with HashMap() instance
	 */
	public static HashMap<Integer, String> getRoomList() {
		HashMap<Integer, String> newRoomList = new HashMap<Integer, String>();

		for (Integer rNo : roomMap.keySet()) {
			newRoomList.put(rNo, roomMap.get(rNo).getRoomName());
		}

		return newRoomList;
	}

	/**
	 * Get opposite user of user me.
	 * @param me
	 * @return Opposite user or null
	 */
	public static User getOppUser(User me) {
		User oppUser = null;
		RoomManager currentRoom = roomMap.get(me.getrNo());
		Vector<User> userList = currentRoom.getUserList();

		for (User u : userList) {
			if (!u.getName().equals(me.getName())) {
				oppUser = u;
				break;
			}
		}
		return oppUser;
	}

	/**
	 * Set first user of each rounds.
	 * @param rNo
	 */
	public static void setStartOfRoom(int rNo) {
		RoomManager currentRoom = roomMap.get(rNo);
		currentRoom.setStarter((int) ((Math.random() * 365) % 2));
	}
}
