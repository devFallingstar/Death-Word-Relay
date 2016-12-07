package Data;

import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class Room implements Serializable {
	private String name;
	private Integer No;
	public Vector<User> roomV = new Vector<User>();
	public Vector<String> roomUsers = new Vector<String>();

	public Room(Integer roomNo, String roomName) {
		this.No = roomNo;
		this.name = roomName;
	}

	public void addUser(User u) {
		this.roomV.add(u);
		this.roomUsers.add(u.getName());
	}

	public void removeUser(User u) {
		this.roomV.remove(u);
		this.roomUsers.remove(u.getName());
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

	public int getRoomMemberNum() {
		return roomV.size();
	}

	public void setRoomV(Vector<User> roomV) {
		this.roomV = roomV;
	}

}
