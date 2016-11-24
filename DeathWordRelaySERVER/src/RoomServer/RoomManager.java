package RoomServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import Data.Room;
import Data.User;

public class RoomManager {
	private Room myRoom;
	private Integer roomNo;
	private String roomName;
	
	private int starter;
	private int nextUser;
	private boolean isStart;
	private List<String> wordList;
	Random rand;
	
	public RoomManager(int _rNo, String _roomName){
		this.roomNo = _rNo;
		this.roomName = _roomName;
		this.starter = 0;
		this.isStart = false;
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
	
	public Vector<User> getUserList(){
		return this.myRoom.getRoomV();
	}
	
	public void setStarter(int startNo){
		if(!isStart){
			starter = startNo;
			
			isStart = true;
		}
	}
	public void startGame(){
		List<User> userArrTmp = new ArrayList<User>();
		
		for (User u : myRoom.getRoomV()){
			userArrTmp.add(u);
		}
		
		System.out.println(starter);
		userArrTmp.get(starter).getOut().println("MYTURN");
		if(starter == 1){
			nextUser = 0;
		}else{
			nextUser = 1;
		}
		wordList = new ArrayList<String>();
	}
	
	public void resumeGame(){
		List<User> userArrTmp = new ArrayList<User>();
		
		for (User u : myRoom.getRoomV()){
			userArrTmp.add(u);
		}
		
		userArrTmp.get(nextUser).getOut().println("MYTURN");
		if(nextUser == 1){
			nextUser = 0;
		}else{
			nextUser = 1;
		}
	}
	public void addPrevWord(String _word){
		wordList.add(_word);
	}
	public boolean isNotDup(String _word){
		for (String prevWord : wordList){
			if(_word.equals(prevWord)){
				return false;
			}
		}
		return true;
	}
}

