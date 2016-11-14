package RoomServer;

import Data.Room;
import Data.User;

public class RoomManager {
	private Room myRoom;
	
	private Integer roomNo;
	private String roomName;
	
	public RoomManager(int _rNo, String _roomName){
		this.roomNo = _rNo;
		this.roomName = _roomName;
		myRoom = new Room(roomNo, roomName);
	}
	
	public void broadCastRoom(String msg){
		for (User u : myRoom.roomV){
			u.getOut().println(msg);
		}
	}
	
	public void addUserToRoom(User u){
		myRoom.addUser(u);
	}
	
	public void removeUserFromRoom(User u){
		myRoom.removeUser(u);
	}
	
	public int playerOfRoom(){
		return myRoom.roomUsers.size();
	}
	
	public Room getMyRoom() {
		return myRoom;
	}

	public Integer getRoomNo() {
		return roomNo;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setMyRoom(Room myRoom) {
		this.myRoom = myRoom;
	}

	public void setRoomNo(Integer roomNo) {
		this.roomNo = roomNo;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}

