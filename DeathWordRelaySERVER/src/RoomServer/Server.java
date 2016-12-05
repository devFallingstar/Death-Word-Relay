package RoomServer;

import java.net.*;
import java.util.*;

import Data.User;

public class Server {
	/*
	 * port for server
	 */
	private static final int PORT = 9001;
	private static final int DATAPORT = 9002;

	private static Vector<User> users = new Vector<User>();
	private static HashMap<Integer, RoomManager> roomMap = new HashMap<Integer, RoomManager>();
	private static ServerSocket dataListener;
	private static ServerSocket listener;

	/**
	 * The appplication main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {
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

	public static void addUser(User u) {
		users.add(u);
	}

	public static void removeUser(User u) {
		users.remove(u);
	}

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

	public static void removeRoom(int roomID) {
		roomMap.remove(roomID);
	}

	public static void addUserToRoom(User u) {
		RoomManager destRoom = roomMap.get(u.getrNo());
		destRoom.addUserToRoom(u);

		destRoom.broadCastRoom("ROOMMSG [SYSTEM] " + u.getName() + " connected.");
		destRoom.broadCastRoom("NEWOPP");
	}

	public static void removeUserFromRoom(User u) {
		RoomManager destRoom = roomMap.get(u.getrNo());
		try {
			destRoom.removeUserFromRoom(u);
			u.setrNo(-1);

			if (destRoom.playerOfRoom() == 0) {
				roomMap.remove(destRoom.getRoomNo());
			} else {
				destRoom.broadCastRoom("ROOMMSG [SYSTEM] " + u.getName() + " disconnected.");
			}
		} catch (NullPointerException e) {
		}
	}

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

	public static RoomManager getRoomWithNumber(int rNo) {
		RoomManager newRoomM = roomMap.get(rNo);

		return newRoomM;
	}

	public static HashMap<Integer, String> getRoomList() {
		HashMap<Integer, String> newRoomList = new HashMap<Integer, String>();

		for (Integer rNo : roomMap.keySet()) {
			newRoomList.put(rNo, roomMap.get(rNo).getRoomName());
		}

		return newRoomList;
	}

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

	public static void setStartOfRoom(int rNo) {
		RoomManager currentRoom = roomMap.get(rNo);
		currentRoom.setStarter((int) ((Math.random() * 365) % 2));
	}
}
