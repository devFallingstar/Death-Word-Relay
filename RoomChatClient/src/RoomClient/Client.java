package RoomClient;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import Data.Room;
import Data.User;
import GUI.*;

public class Client {
	/*
	 * sockets for input and output.
	 */
	public static BufferedReader in;
	public static PrintWriter out;
	Socket socket;
	public static ObjectInputStream objIn;
	public static ObjectOutputStream objOut;
	Socket dataSocket;

	/* personal data */
	String ID, PW;

	public static User myUser;
	public static Room curRoom;

	/* GUI */
	private static Login myLogin;
	private static Waiting myWait;
	private static GameRoom myRoom;

	/**
	 * Runs the client as an application with a closeable frame.
	 */
	public static void main(String[] args) throws Exception {
		Client myClnt = new Client();

		myClnt.run();
	}

	/**
	 * Connects to the server then enters the processing loop.
	 * 
	 * @throws ClassNotFoundException
	 */
	private void run() throws IOException, ClassNotFoundException {
		System.out.println("Running");
		// Make connection and initialize streams
		// String serverAddress = getServerAddress();
		String serverAddress = "127.0.0.1";

		try {
			socket = new Socket(serverAddress, 9001);
			dataSocket = new Socket(serverAddress, 9002);
		} catch (ConnectException ce) {
			System.out.println("Server is down.");
			System.exit(-1);
		}

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		objOut = new ObjectOutputStream(dataSocket.getOutputStream());
		objIn = new ObjectInputStream(dataSocket.getInputStream());

		System.out.println("Streams connected");

		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in.readLine();
			if (!line.equals("NEWROOMAVAIL")) {
				System.out.println(line);
			}

			if (line.startsWith("SUBMITNAME")) {
				myLogin = new Login();

				myLogin.setVisible(true);
			} else if (line.startsWith("NAMEACCEPTED")) {
				myUser = new User(ID, in, out);

				myWait = new Waiting();
				myWait.setVisible(true);
			} else if (line.startsWith("NEWROOMLIST ")) {

			} else if (line.startsWith("COMEINTO ")) {
				StringTokenizer toks = new StringTokenizer(line.substring(9), " ");
				String roomNoStr = toks.nextToken();
				int roomNo = Integer.parseInt(roomNoStr);

				Client.enterToRoom(roomNo);
			} else if (line.startsWith("NEWROOMAVAIL")) {
				try {
					myWait.reloadRoomList();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (line.startsWith("ROOMMADE ")) {
				StringTokenizer toks = new StringTokenizer(line.substring(9), " ");
				String roomNoStr = toks.nextToken();
				int roomNo = Integer.parseInt(roomNoStr);

				Client.getInfoForRoom(roomNo);
				Client.enterToRoom(roomNo);
			} else if (line.startsWith("MESSAGE")) {
				myWait.gotMessage(line);
			}
		}
	}

	public void sendLoginRequest(String _ID, String _PW) throws IOException {
		this.ID = _ID;
		this.PW = _PW;

		if (ID.isEmpty() || PW.isEmpty() || (checkStringPattern(ID) == -1)) {
			myLogin.wrongParam();
		} else {
			out.println(ID + "|" + PW);
			myLogin.setVisible(false);
		}
	}

	public void sendMessage(String msg) throws IOException {
		out.println("MESSAGE " + msg);
	}
	public void sendMessageAtRoom(String msg) throws IOException {
		out.println(msg);
	}

	public static void makeNewRoom() {
		out.println("MAKEROOM " + myWait.getRoomName());
	}

	public static void getInfoForRoom(int rNo) throws ClassNotFoundException, IOException {
		String[] roomInfo = { "", "" };
		myUser.setrNo(rNo);

		out.println("REQROOMINFO " + rNo);

		roomInfo = (String[]) objIn.readObject();

		curRoom = new Room(Integer.parseInt(roomInfo[0]), roomInfo[1]);
	}

	public static void enterToRoom(int rNo) throws IOException, ClassNotFoundException {
		// TODO Room 정보를 받아서 myRoom init 해주기
		myRoom = new GameRoom(curRoom.getNo(), curRoom.getName());
	}

	public static void exitCurrentRoom() {
		out.println("EXITROOM");
		myUser.setrNo(-1);
		myWait = new Waiting();
	}

	public static HashMap<Integer, String> getNewRoomList() throws ClassNotFoundException, IOException {
		HashMap<Integer, String> newRoomList;
		out.println("REQROOMLIST");
		newRoomList = (HashMap<Integer, String>) objIn.readObject();

		return newRoomList;
	}

	/**
	 * Check is there any special character in ID.
	 * 
	 * @param str
	 * @return 0 when there's no special character
	 */
	private int checkStringPattern(String str) {
		if (!str.matches("[0-9|a-z|A-Z]*")) {
			return -1;
		} else {
			return 0;
		}
	}
}
