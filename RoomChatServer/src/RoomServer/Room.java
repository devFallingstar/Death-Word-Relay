package RoomServer;

import java.util.Vector;


public class Room {
	private String name;
	private Integer No;
	Vector<User> roomV = new Vector<User>();

	public Room(Integer roomNo, String roomName) {
		// TODO Auto-generated constructor stub
		this.No = roomNo;
		this.name = roomName;
	}
	
	public void addUser(User u){
		this.roomV.add(u);
	}
	
	public void removeUser(User u){
		this.roomV.remove(u);
	}

}
