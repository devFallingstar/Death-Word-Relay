package RoomClient;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Data.*;
import GUI.*;

public class Client extends JFrame {
	/**
	 * Sockets/Streams for input and output.
	 */
	private static BufferedReader in;
	private static PrintWriter out;
	private Socket socket;
	private static ObjectInputStream objIn;
	private static ObjectOutputStream objOut;
	private Socket dataSocket;

	/**
	 * Personal data
	 */
	private static String ID;
	private static String PW;

	/**
	 * User() and current joining Room() of player
	 */
	public static User curUser;
	public static Room curRoom;

	/**
	 * GUI
	 */
	private static Login myLoginGUI;
	private static Waiting myWaitGUI;
	private static GameRoom myRoomGUI;

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
		try {
			System.out.println("Running...");

			/*
			 * Make connection and initialize streams
			 */
			// String serverAddress = getServerAddress();
			String serverAddress = "127.0.0.1";

			try {
				socket = new Socket(serverAddress, 9001);
				dataSocket = new Socket(serverAddress, 9002);
			} catch (Exception ce) {
				System.out.println("Server is down.");
				serverDownAlert();
				System.exit(-1);
			}

			/*
			 * Streams that send/receive Strings or Objects
			 */
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				objOut = new ObjectOutputStream(dataSocket.getOutputStream());
				objOut.writeInt(0);
				objOut.flush();
				objIn = new ObjectInputStream(dataSocket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * Process all messages from server, according to the protocol.
			 */
			while (true) {
				String line = in.readLine();

				if (line.startsWith("SUBMITNAME")) {
					myLoginGUI = new Login();
					myLoginGUI.setVisible(true);
				} else if (line.startsWith("NAMEACCEPTED")) {
					curUser = new User(ID, in, out);

					myWaitGUI = new Waiting();
					myWaitGUI.setVisible(true);
				} else if (line.startsWith("NEWROOMLIST ")) {

				} else if (line.startsWith("COMEINTO ")) {

					Client.enterToCurrentRoom();
				} else if (line.startsWith("NEWROOMAVAIL")) {
					try {
						Waiting.reloadRoomList();
					} catch (Exception e) {
					}
				} else if (line.startsWith("ROOMMADE ")) {
					StringTokenizer toks = new StringTokenizer(line.substring(9), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);

					Client.getInfoForRoom(roomNo);
					Client.enterToCurrentRoom();
				} else if (line.startsWith("ROOMMSG ")) {
					try {
						System.out.println(line);
						myRoomGUI.gotMessage(line.substring(8));
					} catch (NullPointerException e) {
					}
				} else if (line.startsWith("PLAYGAME")) {
					curUser.setPlaying();
					GameRoom.playGame(curUser.getPlaying());
				} else if (line.startsWith("MYTURN")) {
					GameRoom.enableAnswerField();
				} else if (line.startsWith("IWINROUND")) {
					GameRoom.winNotice();
					GameRoom.myGame.youWin();
				} else if (line.startsWith("ILOSEROUND")) {
					GameRoom.loseNotice();
					GameRoom.myGame.youLose();
				} else if (line.startsWith("SETNEWROUND")) {
					GameRoom.readyForNewRound();
				} else if (line.startsWith("DUPWORD")){
					Lose();
				} else if (line.startsWith("GAMEFIN")){
					GameRoom.gameFin();
				} else if (line.startsWith("MESSAGE ")) {
					Waiting.gotMessage(line.substring(8));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				dataSocket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * To get server address at the start, this function will be used.
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getServerAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP Address of the Server:", "Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * When user can't connect to server, it will be alerted by MessageDialog.
	 */
	private void serverDownAlert() {
		JOptionPane.showMessageDialog(this, "Can't connect to server!");
	}

	/**
	 * When user execute this program, this function will send user inputs(ID
	 * and PW) to the server.
	 * 
	 * @param _ID
	 * @param _PW
	 * @throws IOException
	 */
	public static void sendLoginRequest(String _ID, String _PW) throws IOException {
		ID = _ID;
		PW = _PW;

		if (ID.isEmpty() || PW.isEmpty() || (checkStringPattern(ID) == -1)) {
			myLoginGUI.wrongParam();
		} else {
			out.println(ID + "|" + PW);
			myLoginGUI.setVisible(false);
		}
	}

	/**
	 * This will send user's message to the server when user is in the waiting
	 * room.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public static void sendMessage(String msg) throws IOException {
		out.println("MESSAGE " + msg);
	}

	/**
	 * This will send user's message to the server when user is in the game
	 * room.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public static void sendMessageAtRoom(String msg, int rNo) throws IOException {
		out.println("ROOMMSG " + rNo + " " + msg);
	}

	public static void sendAnswer(String answer, int rNo) {
		out.println("ROOMANS " + rNo + " " + answer);
	}

	public static void requestResume() {
		out.println("REQRESUME");
	}

	public static void sendResult() {
		out.println("GAMERESULT");
	}

	/**
	 * This will send a protocol message to the server, and server response
	 * whether room made or not.
	 */
	public static int makeNewRoom() {
		try {
			String roomName = myWaitGUI.getRoomName();
			if (!roomName.isEmpty()) {
				out.println("MAKEROOM " + roomName);
				return 1;
			} else {
				return -1;
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * Get room's information with room number, by send a protocol message to
	 * the server. It also return duplicated information of the room, and it
	 * sets a user's current room to gotten room.
	 * 
	 * @param rNo
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Room getInfoForRoom(int rNo) throws ClassNotFoundException, IOException {
		String[] roomInfo = { "", "" };
		curUser.setrNo(rNo);

		out.println("REQROOMINFO " + rNo);
		roomInfo = (String[]) objIn.readObject();
		curRoom = new Room(Integer.parseInt(roomInfo[0]), roomInfo[1]);

		return curRoom;

	}

	/**
	 * User enter to the curRoom(current room) with this function. It make new
	 * GameRoom() to build GUI system, and make it with a current room
	 * information. Also, make sure to room GUI is visible.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void enterToCurrentRoom() throws IOException, ClassNotFoundException {
		myRoomGUI = new GameRoom(curRoom.getNo(), curRoom.getName());
		myRoomGUI.setVisible(true);
		out.println("COMESUCC");
	}

	/**
	 * When user press Exit button in Game Room, this function will be called.
	 * This send a protocol message to the server, that means "I'm exiting
	 * current room now!"
	 */
	public static void exitCurrentRoom() {
		out.println("EXITROOM");
		curUser.setrNo(-1);
		myWaitGUI = new Waiting();
	}

	/**
	 * Get new room list by ObjectInputStream(), to make sure if there's a new
	 * rooms or not, with a return value of new rooms list.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, String> getNewRoomList() throws ClassNotFoundException, IOException {
		HashMap<Integer, String> newRoomList;
		out.println("REQROOMLIST");
		newRoomList = (HashMap<Integer, String>) objIn.readObject();

		return newRoomList;
	}

	/**
	 * Check is there any special character in ID, a.k.a non-english.
	 * 
	 * @param str
	 * @return 0 when there's no special character
	 */
	private static int checkStringPattern(String str) {
		if (!str.matches("[0-9|a-z|A-Z]*")) {
			return -1;
		} else {
			return 0;
		}
	}

	public static int submitReg(String ID, String PW, String Nick) {

		return 0;
	}

	public static void AreYouReady(boolean isReady) {
		if (isReady) {
			curUser.setReady();
			out.println("READY");
		} else {
			curUser.setReady();
			out.println("UNREADY");
		}
	}

	public static void GameEnd() {

	}

	public static void Lose() {
		out.println("ILOSEROUND");
	}
}
