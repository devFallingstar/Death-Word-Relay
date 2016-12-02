package RoomServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import Data.Room;
import Data.User;

public class RoomManager {
	private String startWord;
	private Room myRoom;
	private Integer roomNo;
	private String roomName;

	private int starter;
	private int nextUser;
	private boolean isStart;
	private List<String> wordList;
	Random rand;

	private BufferedReader fIn;
	private List<String> startWordList;

	public RoomManager(int _rNo, String _roomName) {
		this.roomNo = _rNo;
		this.roomName = _roomName;
		this.starter = 0;
		this.isStart = false;
		myRoom = new Room(roomNo, roomName);
		try {
			initRandomWordArr();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadCastRoom(String msg) {
		for (User u : myRoom.roomV) {
			u.getOut().println(msg);
		}
	}

	public void addUserToRoom(User u) {
		myRoom.addUser(u);
	}

	public void removeUserFromRoom(User u) {
		myRoom.removeUser(u);
	}

	public int playerOfRoom() {
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

	public Vector<User> getUserList() {
		return this.myRoom.getRoomV();
	}

	public void setStarter(int startNo) {
		if (!isStart) {
			starter = startNo;

			isStart = true;
		}
	}

	public void initRandomWordArr() throws IOException {
		fIn = new BufferedReader(new FileReader("resources/word.txt"));
		String tmp;
		startWordList = new ArrayList<String>();

		while ((tmp = fIn.readLine()) != null) {
			startWordList.add(tmp);
		}
	}

	public String getRandomWord() {
		int i = (int) ((Math.random() * 100) % 50);

		return startWordList.get(i);
	}

	public void startGame() {
		List<User> userArrTmp = new ArrayList<User>();
		String initWord = getRandomWord();
		for (User u : myRoom.getRoomV()) {
			userArrTmp.add(u);
		}
		if (starter == 1) {
			starter = 0;
		} else {
			starter = 1;
		}
		Server.broadCast("The Starter is " + userArrTmp.get(starter).getName(), roomNo);
		Server.broadCast("--------------------------------------------------------------\n", roomNo);
		Server.broadCast("First Word : " + initWord + "\n", roomNo);
		userArrTmp.get(starter).getOut().println("MYTURN");
		if (starter == 1) {
			nextUser = 0;
		} else {
			nextUser = 1;
		}
		wordList = new ArrayList<String>();
		System.out.println(initWord);
		addPrevWord(initWord.trim());
	}

	public void resumeGame() {
		List<User> userArrTmp = new ArrayList<User>();

		for (User u : myRoom.getRoomV()) {
			userArrTmp.add(u);
		}
		userArrTmp.get(nextUser).getOut().println("MYTURN");
		if (nextUser == 1) {
			nextUser = 0;
		} else {
			nextUser = 1;
		}
	}

	public void addPrevWord(String _word) {
		wordList.add(_word);
	}

	public boolean isNotDup(String _word) {
		for (String prevWord : wordList) {
			System.out.println(prevWord);
			if (_word.equals(prevWord)) {
				return false;
			}
		}
		return true;
	}
}
