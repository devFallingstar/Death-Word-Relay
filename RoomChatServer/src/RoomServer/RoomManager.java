package RoomServer;

import java.io.BufferedReader;
import java.util.*;


public class RoomManager {
	Room myRoom;
	
	private Integer roomNo;
	private String roomName;
	
	public RoomManager(int _rNo, String _roomName){
		this.roomNo = _rNo;
		this.roomName = _roomName;
		myRoom = new Room(roomNo, roomName);
	}
	
	public void broadCastRoom(String msg){
		
	}
	
}

