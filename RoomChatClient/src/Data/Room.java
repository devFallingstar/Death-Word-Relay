package Data;

import java.io.Serializable;
import java.util.Vector;

public class Room implements Serializable {
	private String name;
	private Integer No;
	public Vector<User> roomV = new Vector<User>();

	public Room(Integer roomNo, String roomName) {
		this.No = roomNo;
		this.name = roomName;
	}
	
	public void addUser(User u){
		this.roomV.add(u);
	}
	
	public void removeUser(User u){
		this.roomV.remove(u);
	}
	
	public String getName() {
		return name;
	}

	public Integer getNo() {
		return No;
	}

	public Vector<User> getRoomV() {
		return roomV;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNo(Integer no) {
		No = no;
	}

	public void setRoomV(Vector<User> roomV) {
		this.roomV = roomV;
	}

}
